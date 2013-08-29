package com.datasift.client;

import io.higgs.core.func.Function2;
import io.higgs.http.client.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * The API client is a base class which provides a common set of functionality to the other API classes.
 */
public class ApiClient {

    private static final String msg = "Failed to decode a response, can't create instance of the expected result type";
    protected DataSiftConfig config;
    private Logger log = LoggerFactory.getLogger(getClass());

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

    protected <T extends DataSiftResult> Function2<String, io.higgs.http.client.Response> newRequestCallback(
            final FutureData<T> future, final T instance) {
        return new Function2<String, io.higgs.http.client.Response>() {
            public void apply(String s, io.higgs.http.client.Response response) {
                T result = instance;
                if (response.hasFailed()) {
                    result.failed(response.failureCause());
                } else {
                    try {
                        result = (T) DataSiftClient.MAPPER.readValue(s, instance.getClass());
                    } catch (IOException e) {
                        result.failed(e);
                    }
                }
                result.setResponse(new com.datasift.client.Response(s, response));
                future.received(result);
            }
        };
    }
}
