package com.datasift.client.examples;

import com.datasift.client.DataSiftClient;
import com.datasift.client.DataSiftConfig;
import com.datasift.client.core.Stream;
import com.datasift.client.preview.HistoricsPreview;
import org.joda.time.DateTime;

public class PreviewApi {
    private PreviewApi() throws InterruptedException {
        DataSiftConfig config = new DataSiftConfig("username", "api-key");

        final DataSiftClient datasift = new DataSiftClient(config);
        Stream stream = Stream.fromString("13e9347e7da32f19fcdb08e297019d2e");
        DateTime fiveHrsAgo = DateTime.now().minusHours(5);
        HistoricsPreview preview = datasift.preview().create(fiveHrsAgo, stream,
                new String[]{ "interaction.author.link,targetVol,hour;interaction.type,freqDist,10" }).sync();
        //can also later get the preview data
        datasift.preview().get(preview);
        //or
        datasift.preview().get(preview.id());
    }

    public static void main(String... args) throws InterruptedException {
        new PreviewApi();
    }
}
