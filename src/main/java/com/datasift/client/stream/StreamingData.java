package com.datasift.client.stream;

import com.datasift.client.DataSiftClient;
import com.datasift.client.DataSiftConfig;
import com.datasift.client.core.Stream;
import io.higgs.core.func.Function1;
import io.higgs.ws.client.WebSocketClient;
import io.higgs.ws.client.WebSocketEvent;
import io.higgs.ws.client.WebSocketMessage;
import io.higgs.ws.client.WebSocketStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class StreamingData {
    protected URI endpoint;
    protected WebSocketStream liveStream;
    protected DataSiftConfig config;
    protected Map<Stream, StreamSubscription> subscriptions = new NonBlockingHashMap<Stream, StreamSubscription>();
    protected StreamErrorListener listener;

    static {
        //DS produces some fairly big websocket frames. usually 1 or 2 MB max but set to 20 to be sure
        WebSocketClient.maxFramePayloadLength = 20971520; //20 MB
    }

    private Logger log = LoggerFactory.getLogger(getClass());

    public StreamingData(DataSiftConfig config) {
        try {
            endpoint = new URI(String.format("ws://websocket.datasift.com:80/multi?username=%s&api_key=%s",
                    config.getUsername(), config.getApiKey()));
        } catch (URISyntaxException e) {
            log.error("Unable to create endpoint URL", e);
        }
        this.config = config;
    }

    protected void connect() {
        if (liveStream == null) {
            liveStream = WebSocketClient.connect(endpoint);

            liveStream.events().on(new Function1<WebSocketMessage>() {
                public void apply(WebSocketMessage frame) {
                    try {
                        MultiStreamInteraction mi =
                                DataSiftClient.MAPPER.readValue(frame.data(), MultiStreamInteraction.class);
                        if (mi.isDataSiftMessage()) {
                            fireMessage(new DataSiftMessage(mi));
                        } else {
                            if (mi.getData().get("deleted") != null) {
                                listener.onDelete(new DeletedInteraction(mi));
                            } else {
                                fireInteraction(mi.getHash(), new Interaction(mi.getData()));
                            }
                        }
                    } catch (IOException e) {
                        fireError(e);  //unlikely but possible
                    }
                }
            }, WebSocketEvent.MESSAGE);

            liveStream.events().on(new Function1<Throwable>() {
                public void apply(Throwable t) {
                    if (t != null) {
                        fireError(t);
                    }
                }
            }, WebSocketEvent.ERROR);

            liveStream.events().on(new Function1<ChannelHandlerContext>() {
                public void apply(ChannelHandlerContext frame) {
                    listener.streamClosed();
                }
            }, WebSocketEvent.DISCONNECT);

            liveStream.events().on(new Function1<ChannelHandlerContext>() {
                public void apply(ChannelHandlerContext frame) {
                    listener.streamOpened();
                }
            }, WebSocketEvent.CONNECT);
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
            if (message.isInfo()) {
                e.getValue().onDataSiftInfoMessage(message);
            } else if (message.isWarning()) {
                e.getValue().onDataSiftWarning(message);
            } else {
                e.getValue().onDataSiftError(message);
            }
        }
    }

    private void fireError(Throwable e) {
        if (e == null) {
            throw new IllegalArgumentException("Error can't be null!");
        }
        listener.exceptionCaught(e);
    }

    /**
     * Subscribes a callback to listen for exceptions that may occur during streaming.
     * When exceptions occur it is unlikely we'll know which stream/subscription caused the exception
     * so instead of notifying all stream subscribers of the same exception this provides a way to get
     * the error  just once
     *
     * @param listener an error callback
     * @return
     */
    public StreamingData onError(StreamErrorListener listener) {
        this.listener = listener;
        return this;
    }

    public StreamingData subscribe(final StreamSubscription subscription) {
        if (listener == null) {
            throw new IllegalStateException("You must call listen before subscribing to streams otherwise you'll miss" +
                    " deletes and any exceptions that may occur");
        }
        connect();
        liveStream.connectFuture().addListener(new GenericFutureListener<Future<Void>>() {
            public void operationComplete(Future<Void> future) throws Exception {
                if (future.isSuccess()) {
                    subscriptions.put(subscription.stream(), subscription);
                    liveStream.emit(" { \"action\" : \"subscribe\" , \"hash\": \"" + subscription.stream().hash() +
                            "\"}").addListener(new GenericFutureListener<Future<Void>>() {
                        public void operationComplete(Future<Void> f) throws Exception {
                            if (!f.isSuccess()) {
                                fireError(f.cause());
                                subscribe(subscription);
                            }
                        }
                    });
                } else {
                    fireError(future.cause());
                }
            }
        });
        return this;
    }

    public StreamingData unsubscribe(Stream stream) {
        if (stream == null) {
            throw new IllegalArgumentException("Stream can't be null");
        }
        if (stream.hash() == null || stream.hash().isEmpty()) {
            throw new IllegalArgumentException("Invalid stream subscription request, no hash available");
        }
        connect();
        liveStream.emit(" { \"action\" : \"unsubscribe\" , \"hash\": \"" + stream.hash() + "\"}");
        return this;
    }

}
