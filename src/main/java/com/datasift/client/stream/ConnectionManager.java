package com.datasift.client.stream;

import com.datasift.client.DataSiftConfig;
import com.datasift.client.core.Stream;
import io.higgs.ws.client.WebSocketClient;
import org.cliffc.high_scale_lib.NonBlockingHashSet;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class ConnectionManager {
    protected URI endpoint;
    protected DataSiftConfig config;
    protected ErrorListener errorListener;
    protected StreamEventListener streamEventListener;
    protected boolean connected;
    public static boolean detectDeadConnection = true;
    public static int CONNECTION_TIMEOUT_LIMIT = 65;
    public static int CONNECTION_TIMEOUT = 65;
    protected static boolean CONNECTION_DETECTOR_RUNNING;
    protected Logger log = LoggerFactory.getLogger(getClass());
    public static final Set<DataSiftConnection> connections = new NonBlockingHashSet<>();
    protected final int MAX_STREAMS_PER_CONNECTION = 200; //http://dev.datasift.com/docs/streaming-api

    static {
        //DS produces some fairly big websocket frames. usually 1 or 2 MB max but set to 20 to be sure
        WebSocketClient.maxFramePayloadLength = 20971520; //20 MB
        detectDeadConnections();
    }

    /**
     * Starts a thread which checks periodically for dead connections and force them to re-connect.
     * IF AND ONLY IF a thread isn't already running to do this
     */
    public static void detectDeadConnections() {
        if (CONNECTION_DETECTOR_RUNNING) {
            return;
        }
        final Logger log = LoggerFactory.getLogger(ConnectionManager.class);
        new Thread(new Runnable() {
            public void run() {
                CONNECTION_DETECTOR_RUNNING = true;
                try {
                    log.debug("Starting dead connection detection thread");
                    while (detectDeadConnection) {
                        log.debug("Checking if there are any dead connections");
                        long now = DateTime.now().getMillis();
                        for (DataSiftConnection data : connections) {
                            if (data.lastSeen() != null) {
                                if (now - data.lastSeen().getMillis() >=
                                        TimeUnit.SECONDS.toMillis(CONNECTION_TIMEOUT_LIMIT)) {
                                    log.info("Dead connection found, triggering re-connection");
                                    data.closeAndReconnect();
                                }
                            }
                        }
                        log.debug(String.format("Checked %s connections", connections.size()));
                        try {
                            Thread.sleep(TimeUnit.SECONDS.toMillis(CONNECTION_TIMEOUT));
                        } catch (InterruptedException e) {
                            LoggerFactory.getLogger(getClass()).info("Interrupted while waiting to check conn");
                        }
                    }
                } finally {
                    CONNECTION_DETECTOR_RUNNING = false;
                }
            }
        }, "dead-connections-monitor").start();
    }

    public ConnectionManager(DataSiftConfig config) {
        try {
            endpoint = new URI(String.format("%s://%s:%s/multi?username=%s&api_key=%s",
                    (config.isSslEnabled() ? "wss" : "ws"), config.wsHost(),
                    config.port(), config.getUsername(), config.getApiKey()));
        } catch (URISyntaxException e) {
            log.error("Unable to create endpoint URL", e);
        }
        this.config = config;
    }

    protected DataSiftConnection obtainConnectionFor(Stream stream) {
        for (DataSiftConnection conn : connections) {
            if (conn.isFor(stream)) {
                return conn;
            }
        }
        return null;
    }


    /**
     * Subscribes a callback to listen for exceptions that may occur during streaming.
     * When exceptions occur it is unlikely we'll know which stream/subscription caused the exception
     * so instead of notifying all stream subscribers of the same exception this provides a way to list
     * the error  just once
     *
     * @param listener an error callback
     */
    public ConnectionManager onError(ErrorListener listener) {
        this.errorListener = listener;
        for (DataSiftConnection conn : connections) {
            conn.setErrorListener(listener);
        }
        return this;
    }

    public void onStreamEvent(StreamEventListener streamEventListener) {
        this.streamEventListener = streamEventListener;
        for (DataSiftConnection conn : connections) {
            conn.setStreamEventListener(streamEventListener);
        }
    }

    public ConnectionManager subscribe(final StreamSubscription subscription) {
        if (errorListener == null) {
            throw new IllegalStateException("You must call listen before subscribing to streams otherwise you'll miss" +
                    " any exceptions that may occur");
        }
        if (streamEventListener == null) {
            throw new IllegalStateException("You must call onStreamEvent before subscribing to streams otherwise " +
                    "you'll miss delete messages, which you are required to handle");
        }
        DataSiftConnection connection = null;
        for (DataSiftConnection conn : connections) {
            if (conn.canTakeSubscription()) {
                connection = conn;
                break;
            }
        }
        if (connection == null) {
            connection = new DataSiftConnection(MAX_STREAMS_PER_CONNECTION, config, endpoint,
                    errorListener, streamEventListener);
            connections.add(connection);
        }
        connection.subscribe(subscription);
        return this;
    }

    public ConnectionManager unsubscribe(Stream stream) {
        if (stream == null) {
            throw new IllegalArgumentException("Stream can't be null");
        }
        if (stream.hash() == null || stream.hash().isEmpty()) {
            throw new IllegalArgumentException("Invalid stream subscription request, no hash available");
        }
        DataSiftConnection connection = obtainConnectionFor(stream);
        if (connection != null) {
            connection.unsubscribe(stream);
        }
        return this;
    }
}
