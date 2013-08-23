package com.datasift.client;

import io.higgs.core.func.Function2;
import io.higgs.http.client.Request;

import java.io.IOException;

/**
 * The API client is a base class which provides a common set of functionality to the other API classes.
 */
public class ApiClient {

    protected DataSiftConfig config;

    public ApiClient(DataSiftConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("Config cannot be nulll");
        }
        this.config = config;
    }

    public Request applyConfig(Request request) {
        request.header("Authorization", config.authAsHeader());
        return request;
    }

    /**
     * @return a new ParamBuilder instance and automatically adds the required auth properties
     */
    public ParamBuilder newParams() {
        return new ParamBuilder();
    }

    protected <T extends DataSiftResult> Function2<String, io.higgs.http.client.Response> newRequestCallback(final FutureData<T> future, final Class<T> klass) {
        return new Function2<String, io.higgs.http.client.Response>() {
            public void apply(String s, io.higgs.http.client.Response response) {
                T result;
                try {
                    result = DataSiftClient.MAPPER.readValue(s, klass);
                } catch (IOException e) {
                    //we want to gracefully report an error instead of throwing exceptions everywhere so attempt to
                    //create a result object which is marked as failed and contains the response
                    //All result subclasses are required to have a no-arg constructor so it's safe to assume this
                    //if a no-arg constructor doesn't exist the Jackson won't be able to de-serialize anyway
                    try {
                        result = klass.newInstance();
                        result.failedBecauseOf(new FailedResponse(e));
                    } catch (InstantiationException | IllegalAccessException e1) {
                        //what's more useful for debugging here? the original exception e
                        //or the second exception which will indicate why we couldn't create an instance??
                        throw new IllegalStateException("Failed to decode a response and cannot create an instance " +
                                "of the expected result type", e1);
                    }
                }
                result.setResponse(new com.datasift.client.Response(s, response));
                future.received(result);
            }
        };
    }
}
