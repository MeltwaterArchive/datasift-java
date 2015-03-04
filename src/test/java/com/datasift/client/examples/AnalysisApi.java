package com.datasift.client.examples;

import com.datasift.client.DataSiftClient;
import com.datasift.client.DataSiftConfig;
import com.datasift.client.pylon.PylonQueryParameters;
import com.datasift.client.pylon.PylonStream;
import com.datasift.client.pylon.PylonParametersData;
import com.datasift.client.pylon.PylonQuery;
import com.datasift.client.pylon.PylonResult;
import com.datasift.client.pylon.PylonTags;
import com.datasift.client.pylon.PylonStreamStatus;

public class AnalysisApi {
    private AnalysisApi() throws InterruptedException {
        DataSiftConfig config = new DataSiftConfig("zcourts", "44067e0ff342b76b52b36a63eea8e21a");
        DataSiftClient datasift = new DataSiftClient(config);
        PylonStream stream = PylonStream.fromString("13e9347e7da32f19fcdb08e297019d2e");

        // Validation of CSDL
        String csdl = "(fb.content any \"coffee\" OR fb.hashtags in \"tea\") AND fb.language in \"en\"";
        System.out.println(datasift.analysis().validate(csdl));

        // Compilation of CSDL
        PylonStream compiled = datasift.analysis().compile(csdl).sync();
        System.out.println("Compiled object response: " + compiled.toString());

        // Starting a stream for pylon
        String name = "My pylon recording";
        datasift.analysis().start(compiled.hash());

        // Wait 10 seconds for processing
        Thread.sleep(10000);

        // Stopping a stream for pylon
        datasift.analysis().stop(compiled.hash());

        PylonQueryParameters parameters =
                new PylonQueryParameters(
                        "freqDist",
                        new PylonParametersData(
                                null,
                                null,
                                20,
                                "fb.author.gender"
                        )
        );

        PylonQuery query = new PylonQuery(
                compiled.hash(),
                parameters,
                "fb.content contains \"starbucks\"", 0, 100);

        PylonResult result = datasift.analysis().analyze(query).sync();
        System.out.println("Analyze result object response: " + result.toString());

        // Retrieve the pylon
        PylonStreamStatus streamStatus = datasift.analysis().get(compiled.hash()).sync();
        System.out.println("Stream status returned: " + streamStatus.toString());

        // Retrieve VEDO tags for filter
        PylonTags tagsResult = datasift.analysis().tags(compiled.hash()).sync();
        System.out.println("VEDO tags returned for filter: " + tagsResult.getTags().toString());
    }

    public static void main(String... args) throws InterruptedException {
        new AnalysisApi();
    }
}
