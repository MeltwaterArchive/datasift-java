package com.datasift.client.examples;

import com.datasift.client.DataSiftClient;
import com.datasift.client.DataSiftConfig;
import com.datasift.client.core.Stream;
import com.datasift.client.push.PulledInteractions;
import com.datasift.client.push.PushSubscription;
import com.datasift.client.push.pull.LastInteraction;
import com.datasift.client.push.pull.PullJsonType;
import com.datasift.client.stream.Interaction;
import org.joda.time.DateTime;

import java.util.Date;

public class PullApi {
    private PullApi() {
    }

    public static void main(String... args) throws InterruptedException {
        DateTime.now();
        new Date();
        DataSiftConfig config = new DataSiftConfig("zcourts", "acbf4788f875db9fdf6bbd2131b10752");
        final DataSiftClient datasift = new DataSiftClient(config);
        Stream stream = Stream.fromString("13e9347e7da32f19fcdb08e297019d2e");

        PushSubscription streamSubscription = datasift.push()
                .createPull(PullJsonType.JSON_NEW_LINE, stream, "pull test").sync();

        PulledInteractions streamSubscriptions = datasift.push().pull(streamSubscription).sync();

        Interaction interaction;
        //non-blocking loop - if no data is available at the time of request the loop will be exited
        //if using this approach you must remember to manually call streamSubscriptions.stopPulling();
        for (Interaction i : streamSubscriptions) {
            System.out.println(i);
        }
        // recommended : "take" blocks until some data is fetched, the client automatically continues to pull
        //from DataSift until the subscription is finished or failed for some reason.
        //if you want to stop the automatic fetching call streamSubscriptions.stopPulling();
        //alternatively, use the take(upto,time unit) method to specify the maximum time to wait for interactions to
        //become available
        while (!((interaction = streamSubscriptions.take()) instanceof LastInteraction)) {
            System.out.println(interaction);
        }
        System.out.println("We're done!");
    }
}
