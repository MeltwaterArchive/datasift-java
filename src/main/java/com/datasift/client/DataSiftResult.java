package com.datasift.client;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class DataSiftResult {
    @JsonProperty
    protected String error;
    //
    protected Response response;
    protected FailedResponse failedResponse;

    /**
     * A response is considered successful if if a response has been received, the response doesn't contain an error
     * message AND the HTTP response status code is 200 to 399 (i.e. OKs or redirects)
     *
     * @return true if this API result was successful.
     */
    public boolean isSuccessful() {
        return failedResponse == null
                && response != null
                && error == null
                && response.status() > 199
                && response.status() < 400;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public void failedBecauseOf(FailedResponse failedResponse) {
        this.failedResponse = failedResponse;
    }

    /**
     * @return True if the response status is anything but a 401 Unauthorized
     */
    public boolean isAuthorizationSuccesful() {
        return response.status() != 401;
    }

    @Override
    public String toString() {
        return response.toString();
    }
}
