package com.datasift.client.examples;

import com.datasift.client.DataSiftConfig;
import com.datasift.client.FutureData;
import com.datasift.client.pylon.DataSiftPylonTask;
import com.datasift.client.pylon.PylonParametersData;
import com.datasift.client.pylon.PylonQueryParameters;
import com.datasift.client.pylon.PylonTaskAnalysisResult;
import com.datasift.client.pylon.PylonTaskAnalysisResultBucket;
import com.datasift.client.pylon.PylonTaskAnalyzeResponse;
import com.datasift.client.pylon.PylonTaskParameters;
import com.datasift.client.pylon.PylonTaskRequest;
import com.datasift.client.pylon.PylonTaskResult;
import com.datasift.client.pylon.PylonTaskResultList;

import java.util.Scanner;

public class TaskApi {
    private TaskApi() { }

    public static void main(String[] args) {
        /* Making an API client */
        // make a client and ensure we're on version 1.4 of the API
        DataSiftConfig config = new DataSiftConfig("userid", "apikey").versionPrefix("v1.4");
        // construct the API client
        DataSiftPylonTask taskClient = new DataSiftPylonTask(config);

        /* Listing */
        // query 10 tasks
        PylonTaskResultList resultList = taskClient.get(0, 10).sync();
        // print out the total number of tasks the API knows about
        System.out.println("task result count: " + resultList.getCount());
        // print the ones we queried out in a nice table
        for (PylonTaskResult task : resultList.getTasks()) {
            System.out.println("\t" + task.getId() +
                               "\t" + task.getSubscriptionId() +
                               "\t" + task.getType() +
                               "\t" + task.getStatus());
        }

        /* Doing a new Task */
        // build up the frequency distribution query we want
        PylonQueryParameters queryParams = new PylonQueryParameters(
                "freqDist",
                new PylonParametersData("li.all.articles.author.member.age", 10)
        );
        // build the task to do it against a certain time period
        PylonTaskParameters taskParams = new PylonTaskParameters(queryParams, 1488067200, 1488153600);
        // submit the analysis query
        FutureData<PylonTaskAnalyzeResponse> resultFuture = taskClient.analyze(
                new PylonTaskRequest(
                        "subscriptionid",
                        taskParams,
                        "andi-datasift-java",
                        "analysis")
        );
        // pull the ID out of our submission
        String analysisId = resultFuture.sync().getId();
        System.out.println("made request id " + analysisId);
        Scanner s = new Scanner(System.in);
        // loop until we have some results, then display them
        while (true) {
            System.out.println("press enter when you want to retry");
            System.out.println(s.nextLine());
            PylonTaskResult r = taskClient.get(analysisId).sync();
            System.out.println(r.getStatus());
            if (r.getStatus().equals("completed")) {
                System.out.println(r.getResult());
                System.out.println("interactions: " + r.getResult().getInteractions().toString());
                System.out.println("unique authors: " + r.getResult().getUniqueAuthors().toString());
                System.out.println("redacted: " + r.getResult().getAnalysis().getRedacted());
                System.out.println("results:");
                for (PylonTaskAnalysisResultBucket bucket : r.getResult().getAnalysis().getResults()) {
                    System.out.println(
                            bucket.getKey() + " | " +
                            bucket.getInteractions() + " | " +
                            bucket.getUniqueAuthors()
                    );
                }
            }
        }
    }
}
