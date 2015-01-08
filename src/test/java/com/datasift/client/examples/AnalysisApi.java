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

public class AnalysisApi {
    private AnalysisApi() throws InterruptedException {
        DataSiftConfig config = new DataSiftConfig("zcourts", "44067e0ff342b76b52b36a63eea8e21a");
        DataSiftClient datasift = new DataSiftClient(config);
        Stream stream = Stream.fromString("13e9347e7da32f19fcdb08e297019d2e");


    }

    public static void main(String... args) throws InterruptedException {
        new AnalysisApi();
    }
}
