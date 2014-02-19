package com.datasift.client.examples;

import com.datasift.client.DataSiftClient;
import com.datasift.client.DataSiftConfig;
import com.datasift.client.DataSiftResult;
import com.datasift.client.FutureData;
import com.datasift.client.core.Stream;
import com.datasift.client.historics.PreparedHistoricsQuery;
import com.datasift.client.push.PushConnectors;
import com.datasift.client.push.connectors.S3;
import org.joda.time.DateTime;

public class HistoricsApi {
    private HistoricsApi() throws InterruptedException {
        DataSiftConfig config = new DataSiftConfig("zcourts", "44067e0ff342b76b52b36a63eea8e21a");
        DataSiftClient datasift = new DataSiftClient(config);
        Stream stream = Stream.fromString("13e9347e7da32f19fcdb08e297019d2e");

        S3 s3 = PushConnectors
                .s3()
                .accessKey("s3-access-key")
                .secretKey("s3-access-key")
                .bucket("apitests")
                .directory("java-client")
                .acl("private")
                .deliveryFrequency(0)
                .maxSize(10485760)
                .filePrefix("DataSiftJava-");

        DateTime fiveHrsAgo = DateTime.now().minusHours(5);
        DateTime fourHrsAgo = fiveHrsAgo.plusHours(1);
        String name = "My awesome Historics";
        //prepare our query
        PreparedHistoricsQuery query = datasift.historics().prepare(stream.hash(), fiveHrsAgo, fourHrsAgo, name).sync();
        //have to create a push subscription to the newly created historics before starting
        datasift.push().create(s3, FutureData.wrap(query), "Subscription name").sync();
        DataSiftResult historics = datasift.historics().start(query).sync();

        //stop a query
        datasift.historics().stop(query, "some reason").sync();
        //get a single historics query
        datasift.historics().get(query.getId()).sync();
        //get your list of historics
        datasift.historics().list().sync();
        //delete a query
        datasift.historics().delete(query).sync();
    }

    public static void main(String... args) throws InterruptedException {
        new HistoricsApi();
    }
}
