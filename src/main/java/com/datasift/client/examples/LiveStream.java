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

public class LiveStream {
    public static void main(String... args) throws InterruptedException {
        DataSiftConfig config = new DataSiftConfig("username", "api-key");
        final DataSiftClient datasift = new DataSiftClient(config);

        Stream stream = Stream.fromString("13e9347e7da32f19fcdb08e297019d2e");

        //handle exceptions that can't necessarily be linked to a specific stream
        datasift.liveStream().onError(new ErrorHandler());

        //handle delete message
        datasift.liveStream().onStreamEvent(new DeleteHandler());

        //process interactions
        datasift.liveStream().subscribe(new Subscription(stream));
        //process interactions for another stream
        datasift.liveStream().subscribe(new Subscription(Stream.fromString("another-stream-hash")));

        //at some point later if you want unsubscribe
        datasift.liveStream().unsubscribe(stream);
    }

    public static class Subscription extends StreamSubscription {
        public Subscription(Stream stream) {
            super(stream);
        }

        public void onDataSiftLogMessage(DataSiftMessage di) {
            //di.isWarning() is also available
            System.out.println((di.isError() ? "Error" : di.isInfo() ? "Info" : "Warning") + ":\n" + di);
        }

        public void onMessage(Interaction i) {
            System.out.println("INTERACTION:\n" + i);
        }
    }

    public static class DeleteHandler extends StreamEventListener {
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