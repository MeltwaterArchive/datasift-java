package com.datasift.client.examples;

import com.datasift.client.DataSiftClient;
import com.datasift.client.DataSiftConfig;
import com.datasift.client.DataSiftResult;
import com.datasift.client.FutureData;
import com.datasift.client.analysis.AnalysisStreamStatus;
import com.datasift.client.analysis.AnalysisTags;
import com.datasift.client.analysis.AnalyzeQuery;
import com.datasift.client.analysis.AnalyzeResult;
import com.datasift.client.core.Stream;
import com.datasift.client.historics.PreparedHistoricsQuery;
import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.joda.time.DateTime;

import java.util.Map;

public class AnalysisApi {
    private AnalysisApi() throws InterruptedException {
        DataSiftConfig config = new DataSiftConfig("zcourts", "44067e0ff342b76b52b36a63eea8e21a");
        DataSiftClient datasift = new DataSiftClient(config);
        Stream stream = Stream.fromString("13e9347e7da32f19fcdb08e297019d2e");

        // Validation of CSDL
        String csdl = "(fb.content any \"coffee\" OR fb.hashtags in \"tea\") AND fb.language in \"en\"";
        System.out.println(datasift.analysis().validate(csdl));

        // Compilation of CSDL
        Stream compiled = datasift.analysis().compile(csdl).sync();
        System.out.println("Compiled object response: " + compiled.toString());

        // Starting a stream for analysis
        String name = "My analysis recording";
        datasift.analysis().start(compiled.hash());

        // Wait 10 seconds for processing
        Thread.sleep(10000);

        // Stopping a stream for analysis
        datasift.analysis().stop(compiled.hash());

        Map<String, Object> aggregatorParameters = new NonBlockingHashMap<String, Object>();
        Map<String, Object> parameters = new NonBlockingHashMap<String, Object>();

        parameters.put("threshold", 5);
        parameters.put("target", "fb.author.age");
        aggregatorParameters.put("analysis_type", "freqDist");
        aggregatorParameters.put("parameters", parameters);

        AnalyzeQuery query = new AnalyzeQuery(compiled.hash(), aggregatorParameters, "fb.content contains \"starbucks\"", 0, 100);
        AnalyzeResult result = datasift.analysis().analyze(query).sync();
        System.out.println("Analyze result object response: " + result.toString());

        // Retrieve the analysis
        AnalysisStreamStatus streamStatus = datasift.analysis().get(compiled.hash()).sync();
        System.out.println("Stream status returned: " + streamStatus.toString());

        // Retrieve VEDO tags for filter
        AnalysisTags tagsResult = datasift.analysis().tags(compiled.hash()).sync();
        System.out.println("VEDO tags returned for filter: " + tagsResult.getTags().toString());
    }

    public static void main(String... args) throws InterruptedException {
        new AnalysisApi();
    }
}
