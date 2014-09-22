package com.datasift.client.examples;

import com.datasift.client.DataSiftClient;
import com.datasift.client.DataSiftConfig;
import com.datasift.client.core.Stream;
import com.datasift.client.stream.DataSiftMessage;
import com.datasift.client.stream.DeletedInteraction;
import com.datasift.client.stream.ErrorListener;
import com.datasift.client.stream.Interaction;
import com.datasift.client.stream.StreamEventListener;
import com.datasift.client.stream.StreamSubscription;

import java.util.concurrent.atomic.AtomicLong;

public class LiveStream {
    private LiveStream() {
    }

    public static void main(String... args) throws InterruptedException {
        DataSiftConfig config = new DataSiftConfig("zcourts", "a9641ee0770d05ff3d83225c9fafe4bb");
        final DataSiftClient datasift = new DataSiftClient(config);
        Stream result = datasift.compile("interaction.content contains \"music\" ").sync();
        //handle exceptions that can't necessarily be linked to a specific stream
        datasift.liveStream().onError(new ErrorHandler());

        //handle delete message
        datasift.liveStream().onStreamEvent(new ExampleStreamEventHandler());

        //process interactions
        datasift.liveStream().subscribe(new Subscription(Stream.fromString("67b378312cca82eaf9fdaa9c3880c440")));
        //process interactions for another stream
        datasift.liveStream().subscribe(new Subscription(Stream.fromString("another-stream-hash")));
        datasift.liveStream().subscribe(new Subscription(result));
        //at some point later if you want unsubscribe
        datasift.liveStream().unsubscribe(Stream.fromString("67b378312cca82eaf9fdaa9c3880c450"));
    }

    public static class Subscription extends StreamSubscription {
        AtomicLong count = new AtomicLong();

        public Subscription(Stream stream) {
            super(stream);
        }

        public void onDataSiftLogMessage(DataSiftMessage di) {
            //di.isWarning() is also available
            System.out.println((di.isError() ? "Error" : di.isInfo() ? "Info" : "Warning") + ":\n" + di);
        }

        public void onMessage(Interaction i) {
            if (count.incrementAndGet() % 100 == 0) {
                System.out.println(count.get() + " <> INTERACTION:\n" + i);
            }
        }
    }

    public static class ExampleStreamEventHandler extends StreamEventListener {
        public void streamClosed() {
            System.out.println("Connection closed");
        }

        public void streamOpened() {
            System.out.println("Connected");
        }

        public void onDelete(DeletedInteraction di) {
            //go off and delete the interaction if you have it stored. This is a strict requirement!
            System.out.println("DELETED:\n " + di);
        }
    }

    public static class ErrorHandler extends ErrorListener {
        public void exceptionCaught(Throwable t) {
            t.printStackTrace();
            //do something useful...
        }
    }
}
