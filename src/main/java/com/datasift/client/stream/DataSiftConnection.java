package com.datasift.client.stream;

import com.datasift.client.DataSiftClient;
import com.datasift.client.DataSiftConfig;
import com.datasift.client.core.Stream;
import io.higgs.ws.client.WebSocketClient;
import io.higgs.ws.client.WebSocketEventListener;
import io.higgs.ws.client.WebSocketMessage;
import io.higgs.ws.client.WebSocketStream;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.util.concurrent.GenericFutureListener;
import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.cliffc.high_scale_lib.NonBlockingHashSet;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Courtney Robinson <courtney@crlog.info>
 */
class DataSiftConnection implements WebSocketEventListener {
    protected final DataSiftConfig config;
    protected final URI endpoint;
    protected WebSocketStream connection;
    protected int maxAllowedSubscriptions;
    protected StreamEventListener streamEventListener;
    protected ErrorListener errorListener;
    protected short MAX_TIMEOUT = 320, currentTimeout;
    protected DateTime lastSeen;
    protected Map<Stream, StreamSubscription> subscriptions = new NonBlockingHashMap<>();
    protected final Set<StreamSubscription> unsentSubscriptions = new NonBlockingHashSet<>();
    protected Logger log = LoggerFactory.getLogger(getClass());
    protected boolean handshakeCompleted;

    protected DataSiftConnection(int maxAllowedSubscriptions, DataSiftConfig config, URI endpoint,
                                 ErrorListener errorListener, StreamEventListener streamEventListener) {
        this.maxAllowedSubscriptions = maxAllowedSubscriptions;
        this.config = config;
        this.endpoint = endpoint;
        this.errorListener = errorListener;
        this.streamEventListener = streamEventListener;
        connect();
    }

    protected DateTime lastSeen() {
        return lastSeen;
    }

    protected void setStreamEventListener(StreamEventListener streamEventListener) {
        this.streamEventListener = streamEventListener;
    }

    protected void setErrorListener(ErrorListener errorListener) {
        this.errorListener = errorListener;
    }

    protected synchronized boolean canTakeSubscription() {
        return (unsentSubscriptions.size() + subscriptions.size()) <= maxAllowedSubscriptions;
    }

    protected void subscribe(final StreamSubscription subscription) {
        synchronized (unsentSubscriptions) {
            unsentSubscriptions.add(subscription);
        }
        pushUnsentSubscriptions();
    }

    protected synchronized void pushUnsentSubscriptions() {
        for (final StreamSubscription subscription : unsentSubscriptions) {
            if (connection == null || !connection.channel().isActive()) {
                connect();
                return;
            }
            if (!handshakeCompleted) {
                return;
            }
            connection.connectFuture().syncUninterruptibly();
            connection.send("{\"action\":\"subscribe\",\"hash\":\"" + subscription.stream().hash() + "\"}")
                    .addListener(new GenericFutureListener<ChannelFuture>() {
                        public void operationComplete(ChannelFuture future) throws Exception {
                            if (future.isSuccess()) {
                                subscriptions.put(subscription.stream(), subscription);
                                unsentSubscriptions.remove(subscription);
                            } else {
                                fireError(future.cause());
                                subscribe(subscription);
                            }
                        }
                    });
        }
    }

    protected void unsubscribe(final Stream stream) {
        if (connection == null) { //if true there's no need to unsubscribe
            connect();
            return;
        }
        connection.send(" { \"action\" : \"unsubscribe\" , \"hash\": \"" + stream.hash() + "\"}");
        subscriptions.remove(stream);
        for (StreamSubscription subscription : unsentSubscriptions) {
            if (stream.isSameAs(subscription.stream())) {
                unsentSubscriptions.remove(subscription);
            }
        }
    }

    @Override
    public void onConnect(ChannelHandlerContext ctx) {
        synchronized (this) {
            handshakeCompleted = true;
        }
        streamEventListener.streamOpened();
        pushUnsentSubscriptions();
    }

    @Override
    public void onClose(ChannelHandlerContext ctx, CloseWebSocketFrame frame) {
        synchronized (this) {
            handshakeCompleted = false;
        }
        streamEventListener.streamClosed();
        //re-subscribe
        unsentSubscriptions.addAll(subscriptions.values());
        subscriptions.clear();
        connect();
    }

    protected void closeAndReconnect() {
        try {
            connection.channel().close();
        } finally {
            connect();
        }
    }

    protected void connect() {
        if (connection != null && connection.channel().isActive()) {
            return;
        }
        if (currentTimeout < 1) {
            currentTimeout = 1;
        } else {
            if (config.isAutoReconnect() &&
                    TimeUnit.SECONDS.toMillis(currentTimeout) <= TimeUnit.SECONDS.toMillis(MAX_TIMEOUT)) {
                currentTimeout *= 2;
            }
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(currentTimeout));
            } catch (InterruptedException ignored) {
                log.info("Sleep interrupted, reconnecting");
            }
        }
        connection = WebSocketClient.connect(endpoint, false, config.sslProtocols());
        connection.subscribe(this);
        connection.connectFuture().syncUninterruptibly();
    }

    @Override
    public void onPing(ChannelHandlerContext ctx, PingWebSocketFrame frame) {
        //only on ping or message should we reset the timeout
        currentTimeout = 1;
        lastSeen = DateTime.now();
    }

    @Override
    public void onMessage(ChannelHandlerContext ctx, WebSocketMessage msg) {
        currentTimeout = 1;
        lastSeen = DateTime.now();
        try {
            MultiStreamInteraction mi =
                    DataSiftClient.MAPPER.readValue(msg.data(), MultiStreamInteraction.class);
            if (mi.isDataSiftMessage()) {
                fireMessage(new DataSiftMessage(mi));
            } else {
                if (mi.getData().get("deleted") != null) {
                    streamEventListener.onDelete(new DeletedInteraction(mi));
                } else {
                    fireInteraction(mi.getHash(), new Interaction(mi.getData()));
                }
            }
        } catch (IOException e) {
            fireError(e);  //unlikely but possible
        }
    }

    @Override
    public void onError(ChannelHandlerContext ctx, Throwable cause, FullHttpResponse response) {
        if (cause != null) {
            fireError(cause);
        }
    }

    protected void fireInteraction(String hash, Interaction interaction) {
        for (Map.Entry<Stream, StreamSubscription> e : subscriptions.entrySet()) {
            if (e.getKey().isSameAs(hash)) {
                e.getValue().onMessage(interaction);
            }
        }
    }

    protected void fireMessage(DataSiftMessage message) {
        if (message == null) {
            throw new IllegalArgumentException("Message can't be null!");
        }
        for (Map.Entry<Stream, StreamSubscription> e : subscriptions.entrySet()) {
            if (message.hashHashes()) {
                //if we have hashes then only subscriptions for each hash should be notified
                for (Stream hash : message.hashes()) {
                    if (e.getKey().isSameAs(hash)) {
                        e.getValue().onDataSiftLogMessage(message);
                    }
                }
            } else {
                //otherwise let everyone know
                e.getValue().onDataSiftLogMessage(message);
            }
        }
    }

    protected void fireError(Throwable e) {
        if (e == null) {
            throw new IllegalArgumentException("Error can't be null!");
        }
        errorListener.exceptionCaught(e);
    }

    protected boolean isFor(Stream stream) {
        return subscriptions.get(stream) != null;
    }
}
