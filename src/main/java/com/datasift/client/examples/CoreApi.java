package com.datasift.client.examples;

import com.datasift.client.*;
import com.datasift.client.core.*;

public class CoreApi {
    private CoreApi() {
    }

    public static void main(String... args) throws InterruptedException {
        DataSiftConfig config = new DataSiftConfig("username", "api-key");
        //or
        config = new DataSiftConfig().auth("zcourts", "44067e0ff342b76b52b36a63eea8e21a");

        final DataSiftClient datasift = new DataSiftClient(config);
        String csdl = "interaction.content contains \"some string\"";
        //both sync and async processing are supported by calling "sync" on any FutureDate object

        //all response objects extend DataSiftResult which present these utility methods
        DataSiftResult result = datasift.compile(csdl).sync();
        //is successful returns true if a response hasn't explicitly been marked as failed,
        //there is a valid response, no exceptions are set and the response status is between 200 - 399
        if (result.isSuccessful()) {
            //if true an exception may have caused the request to fail, inspect the cause if available
            if (result.failureCause() != null) { //may not be an exception
                result.failureCause().printStackTrace();
            }
            return;
        }
        //is true if isSuccessful() == true and the response status is not 401
        result.isAuthorizationSuccesful();
        //allows access to the response object which you can list the request and JSON string response from
        result.getResponse();
        //gets the rate limit DataSift returned with the response, use it to keep track of usage
        result.rateLimit();
        //returns the cost of executing the request which produced this result
        result.rateLimitCost();
        //what's left of your rate limit quota
        result.rateLimitRemaining();

        Usage usage = datasift.usage().sync();

        Stream stream = Stream.fromString("13e9347e7da32f19fcdb08e297019d2e");
        Dpu dpu = datasift.dpu(stream).sync();

        Balance balance = datasift.balance().sync();
        //synchronously validate a CSDL
        Validation validation = datasift.validate(csdl).sync();
        if (!validation.isSuccessful()) {
            //if true an exception may have caused the request to fail, inspect the cause if available
            if (validation.failureCause() != null) { //may not be an exception
                validation.failureCause().printStackTrace();
            }
            return;
        }
        System.out.println(validation);
        if (validation.isSuccessful()) {
            //we now know it's valid so asynchronously compile the CSDL and obtain a stream
            FutureData<Stream> compiledStream = datasift.compile(csdl);
            compiledStream.onData(new FutureResponse<Stream>() {
                public void apply(Stream data) {
                    System.out.println(data);
                }
            });
            FutureData<Dpu> dpus = datasift.dpu(compiledStream);
            dpus.onData(new FutureResponse<Dpu>() {
                public void apply(Dpu data) {
                    System.out.println(data);
                }
            });
        }
        System.out.println(datasift.balance().sync());
        System.out.println(datasift.usage().sync());
    }
}
