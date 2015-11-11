package com.datasift.client.examples;

import com.datasift.client.DataSiftClient;
import com.datasift.client.DataSiftConfig;
import com.datasift.client.accounts.Usage;
import com.datasift.client.accounts.UsageResult;
import com.datasift.client.pylon.PylonQueryParameters;
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

        // Starting a recording for pylon
        String name = "My pylon recording";
        datasift.pylon().start(compiled.hash());

        // Wait 10 seconds for processing
        Thread.sleep(10000);

        // Stopping a recording for pylon
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

        // Retrieve the pylon recording status
        PylonStreamStatus streamStatus = datasift.pylon().get(compiled.hash()).sync();
        System.out.println("Stream status returned: " + streamStatus.toString());

        // Retrieve VEDO tags for filter
        PylonTags tagsResult = datasift.pylon().tags(compiled.hash()).sync();
        System.out.println("VEDO tags returned for filter: " + tagsResult.getTags().toString());

        // Retrieve account usage to monitor identities
        UsageResult accountUsage = datasift.account().getUsage("hourly", 0, 0).sync();
        for (Iterator<Usage> i = accountUsage.getUsageList().iterator(); i.hasNext();) {
            Usage u = i.next();
            System.out.println("Usage record: ");
            System.out.print("Category: " + u.category());
            System.out.print("Source: " + u.source());
            System.out.print("Cost: " + u.cost());
            System.out.print("Quantity: " + u.quantity());
            System.out.print("Timestamp: " + u.timestamp() + "\n");
        }
    }

    public static void main(String... args) throws InterruptedException {
        new PylonApi();
    }
}
