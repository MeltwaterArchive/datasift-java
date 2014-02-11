package com.datasift.client;

import com.datasift.client.examples.JsonParsingException;
import com.datasift.client.exceptions.AuthException;
import com.datasift.client.exceptions.DataSiftException;
import io.higgs.core.func.Function2;
import io.higgs.http.client.Request;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.io.IOException;

/**
 * The API client is a base class which provides a common set of functionality to the other API classes.
 */
public class DataSiftApiClient {

    protected DataSiftConfig config;

    public DataSiftApiClient(DataSiftConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("Config cannot be nulll");
        }
        this.config = config;
    }

    public Request applyConfig(Request request) {
        request.header("Authorization", config.authAsHeader());
        request.withSSLProtocols(config.sslProtocols());
        return request;
    }

    /**
     * @return a new ParamBuilder instance and automatically adds the required auth properties
     */
    public ParamBuilder newParams() {
        return new ParamBuilder();
    }

    protected <T extends DataSiftResult> Function2<String, io.higgs.http.client.Response> newRequestCallback(
            final FutureData<T> future, final T instance, final DataSiftConfig config) {
        return new Function2<String, io.higgs.http.client.Response>() {
            public void apply(String s, io.higgs.http.client.Response response) {
                T result = instance;
                if (response.getStatus() != null && HttpResponseStatus.NO_CONTENT.equals(response.getStatus())) {
                    //if a 204 is returned don't attempt to parse a JSON out of it,
                    // since there shouldn't be any content as the status implies
                    result.successful();
                } else if (response.hasFailed()) {
                    result.failed(response.failureCause());
                    if (config.isAllowedToRaiseExceptions()) {
                        throw new DataSiftException("API request failed", response.failureCause(), response);
                    }
                } else {
                    try {
                        result = (T) DataSiftClient.MAPPER.readValue(s, instance.getClass());
                    } catch (IOException e) {
                        result.failed(e);
                        if (config.isAllowedToRaiseExceptions()) {
                            throw new JsonParsingException("Unable to decode JSON from DataSift response", e, response);
                        }
                    }
                }
                result.setResponse(new com.datasift.client.Response(s, response));
                if (config.isAllowedToRaiseExceptions() && response.getStatus().code() == 401) {
                    throw new AuthException("Please provide a valid username and API key", response);
                }
                future.received(result);
            }
        };
    }

    /**
     * To support futures being passed as parameters, this method adds a listener to the unprocessed future that has
     * been passed as a parameter. Once that listener is invoked, the response of the unprocessed future is examined
     * to see if the response was successful, if it was not then the expected future is passed the failed response
     * If the result of the unprocessed future is successful then the response callback is applied.
     *
     * @param futureToUnwrap             the unprocessed future which needs to be unwrapped
     * @param futureReturnedToUser       the future that has been returned to the user and which callbacks need to be
     *                                   triggered on
     * @param expectedInstance           the instance of the result type to use in failure scenarios
     * @param responseToExecuteOnSuccess a future response object which contains the code which will execute once the
     *                                   wrapped future has been unwrapped and its result is successful
     * @param <T>
     * @param <A>
     */
    protected <T extends DataSiftResult, A extends DataSiftResult> void unwrapFuture(FutureData<T> futureToUnwrap,
                                                                                     final FutureData<A>
                                                                                             futureReturnedToUser,
                                                                                     final A expectedInstance,
                                                                                     final FutureResponse<T>
                                                                                             responseToExecuteOnSuccess
    ) {
        futureToUnwrap.onData(new FutureResponse<T>() {
            public void apply(T stream) {
                if (stream.isSuccessful()) {
                    responseToExecuteOnSuccess.apply(stream);
                } else {
                    expectedInstance.setResponse(stream.getResponse());
                    futureReturnedToUser.received(expectedInstance);
                }
            }
        });
    }

}
