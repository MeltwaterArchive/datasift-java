package com.datasift.client.examples;

import com.datasift.client.DataSiftClient;
import com.datasift.client.DataSiftConfig;
import com.datasift.client.core.Stream;
import com.datasift.client.historics.PreparedHistoricsQuery;
import com.datasift.client.push.PulledInteractions;
import com.datasift.client.push.PushSubscription;
import com.datasift.client.stream.Interaction;

public class PullApi {
    private PullApi() {
    }

    public static void main(String... args) throws InterruptedException {
        DataSiftConfig config = new DataSiftConfig("zcourts", "603445311219283431f7637deb5ab1df");
        final DataSiftClient datasift = new DataSiftClient(config);
        Stream stream = Stream.fromString("7a33885e8c69d96cdd47bcddf381002b");
        PreparedHistoricsQuery historic = PreparedHistoricsQuery.fromString("e34385d5a2a0091287ae");

        PushSubscription streamSubscription = datasift.push().createPull(stream, "pull test").sync();
        PushSubscription historicsSubscription = datasift.push().createPull(historic, "pull test").sync();

        PulledInteractions streamSubscriptions = datasift.push().pull(streamSubscription).sync();
        for (Interaction i : streamSubscriptions.getInteractions()) {
            System.out.println(i);
        }

        PulledInteractions historicSubscriptions = datasift.push().pull(historicsSubscription).sync();
        for (Interaction i : historicSubscriptions.getInteractions()) {
            System.out.println(i);
        }
    }
}
