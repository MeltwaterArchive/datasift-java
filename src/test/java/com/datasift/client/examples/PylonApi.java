package com.datasift.client.examples;

import com.datasift.client.DataSiftClient;
import com.datasift.client.DataSiftConfig;
import com.datasift.client.pylon.PylonQueryParameters;
import com.datasift.client.pylon.PylonSample;
import com.datasift.client.pylon.PylonSampleInteraction;
import com.datasift.client.pylon.PylonSampleInteractionItem;
import com.datasift.client.pylon.PylonSampleRequest;
import com.datasift.client.pylon.PylonStream;
import com.datasift.client.pylon.PylonParametersData;
import com.datasift.client.pylon.PylonQuery;
import com.datasift.client.pylon.PylonResult;
import com.datasift.client.pylon.PylonTags;
import com.datasift.client.pylon.PylonStreamStatus;

import java.util.Iterator;

public class PylonApi {
    private PylonApi() throws InterruptedException {
        DataSiftConfig config = new DataSiftConfig("zcourts", "44067e0ff342b76b52b36a63eea8e21a");
        DataSiftClient datasift = new DataSiftClient(config);
        PylonStream stream = PylonStream.fromString("13e9347e7da32f19fcdb08e297019d2e");

        // Validation of CSDL
        String csdl = "(fb.content any \"coffee\" OR fb.hashtags in \"tea\") AND fb.language in \"en\"";
        System.out.println(datasift.pylon().validate(csdl));

        // Compilation of CSDL
        PylonStream compiled = datasift.pylon().compile(csdl).sync();
        System.out.println("Compiled object response: " + compiled.toString());

        // Starting a stream for pylon
        String name = "My pylon recording";
        datasift.pylon().start(compiled.hash());

        // Wait 10 seconds for processing
        Thread.sleep(10000);

        // Stopping a stream for pylon
        datasift.pylon().stop(compiled.hash());

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

        // Nested query to depth of two
        PylonQueryParameters parametersNested =
                new PylonQueryParameters(
                        "freqDist",
                        new PylonParametersData(
                                null,
                                null,
                                20,
                                "fb.author.country"
                        ),
                        new PylonQueryParameters(
                                "freqDist",
                                new PylonParametersData(
                                        null,
                                        null,
                                        2,
                                        "fb.author.gender"
                                ),
                                new PylonQueryParameters(
                                        "freqDist",
                                        new PylonParametersData(
                                                null,
                                                null,
                                                2,
                                                "fb.author.gender"
                                        )
                                )
                        )
                );

        PylonQuery query = new PylonQuery(
                compiled.hash(),
                parameters,
                "fb.content contains \"starbucks\"", 0, 100
        );

        PylonQuery queryNested = new PylonQuery(
                compiled.hash(),
                parametersNested,
                "fb.content contains \"starbucks\"", 0, 100
        );

        PylonResult result = datasift.pylon().analyze(query).sync();
        System.out.println("Analyze result object response: " + result.toString());

        PylonResult resultNested = datasift.pylon().analyze(queryNested).sync();
        System.out.println("Analyze nested result object response: " + result.toString());

        // Retrieve the pylon
        PylonStreamStatus streamStatus = datasift.pylon().get(compiled.hash()).sync();
        System.out.println("Stream status returned: " + streamStatus.toString());

        // Retrieve VEDO tags for filter
        PylonTags tagsResult = datasift.pylon().tags(compiled.hash()).sync();
        System.out.println("VEDO tags returned for filter: " + tagsResult.getTags().toString());

        // Sample a Pylon stream
        PylonSampleRequest sampleRequest = new PylonSampleRequest(
                compiled.hash(),
                100,
                "fb.content contains \"coffee\""
        );

        PylonSample sampleResult = datasift.pylon().sample(sampleRequest).sync();
        for (Iterator<PylonSampleInteractionItem> i = sampleResult.getInteractions().iterator(); i.hasNext();) {
            PylonSampleInteractionItem s = i.next();
            System.out.println("Sample: " );
            System.out.print("Base interaction properties: " + s.getInteractionParent().toString());
            System.out.print("fb interaction properties: " + s.getInteraction().toString());
            System.out.print("Number of fb interaction Topic IDs: " + s.getInteraction().getTopicIDs().size() + "\n");
        }
    }

    public static void main(String... args) throws InterruptedException {
        new PylonApi();
    }
}
