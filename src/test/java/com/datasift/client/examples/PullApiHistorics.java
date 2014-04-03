package com.datasift.client.examples;

import com.datasift.client.DataSiftClient;
import com.datasift.client.DataSiftConfig;
import com.datasift.client.core.Stream;
import com.datasift.client.historics.PreparedHistoricsQuery;
import com.datasift.client.push.PulledInteractions;
import com.datasift.client.push.PushSubscription;
import com.datasift.client.push.pull.LastInteraction;
import com.datasift.client.push.pull.PullJsonType;
import com.datasift.client.stream.Interaction;
import org.joda.time.DateTime;

public class PullApiHistorics {
    private PullApiHistorics() {
    }

    public static void main(String... args) throws InterruptedException {
        DataSiftConfig config = new DataSiftConfig("zcourts", "acbf4788f875db9fdf6bbd2131b10752");
        final DataSiftClient datasift = new DataSiftClient(config);
        Stream stream = Stream.fromString("13e9347e7da32f19fcdb08e297019d2e");

        PreparedHistoricsQuery historic = datasift.historics().prepare(stream.hash(), DateTime.now().minusHours(5),
                DateTime.now().minusHours(4), "Historics pull test").sync();

        PushSubscription historicsSubscription = datasift.push()
                .createPull(PullJsonType.JSON_NEW_LINE, historic, "pull test " + DateTime.now()).sync();

        //don't forget to start it, AFTER creating the pull subscription
        datasift.historics().start(historic).sync();

        PulledInteractions historicSubscriptions = datasift.push().pull(historicsSubscription).sync();

        Interaction interaction;
        while (!((interaction = historicSubscriptions.take()) instanceof LastInteraction)) {
            System.out.println(interaction);
        }
        System.out.println("We're done!");
    }
}
