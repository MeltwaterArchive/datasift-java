package com.datasift.client.examples;

import com.datasift.client.DataSiftClient;
import com.datasift.client.DataSiftConfig;
import com.datasift.client.FutureData;
import com.datasift.client.core.Stream;
import com.datasift.client.push.PushConnectors;
import com.datasift.client.push.PushSubscription;
import com.datasift.client.push.connectors.S3;

public class PushApi {
    private PushApi() {
    }

    public static void main(String... args) throws InterruptedException {
        DataSiftConfig config = new DataSiftConfig("zcourts", "44067e0ff342b76b52b36a63eea8e21a");
        final DataSiftClient datasift = new DataSiftClient(config);
        Stream stream = Stream.fromString("13e9347e7da32f19fcdb08e297019d2e");
        S3 s3 = PushConnectors.s3()
                .accessKey("amazon-access-key")
                .secretKey("amazone-secret")
                .maxSize(102400)
                .deliveryFrequency(0)
                .acl("acl")
                .bucket("some-bucket")
                .directory("abc");
        FutureData<PushSubscription> subscriptionFuture = datasift.push().create(
                s3,
                null,  //historics must be null, we're creating from a stream
                FutureData.wrap(stream),
                "Example push from stream"
        );
    }
}
