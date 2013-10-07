package com.datasift.client;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class DataSiftResult {
    @JsonProperty
    protected String error;
    //
    protected Response response;
    private Throwable cause;
    private boolean failed;

    /**
     * A response is considered successful if if a response has been received, the response doesn't contain an error
     * message AND the HTTP response status code is 200 to 399 (i.e. OKs or redirects)
     *
     * @return true if this API result was successful.
     */
    public boolean isSuccessful() {
        return !hasFailed()
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
        //setResponse can happen after a result has been marked as failed
        //so if it is already failed then keep it as a failure
        failed = failed ? failed : response.hasFailed();
        //likewise if already failed keep the existing cause
        cause = cause != null ? cause : response.failureCause();
    }

    /**
     * @return true if the request for this response has failed for some reason
     */
    public boolean hasFailed() {
        return failed;
    }

    /**
     * @return If the request has failed, this returns the reason for the failure
     *         May* be null
     */
    public Throwable failureCause() {
        return cause;
    }

    /**
     * @return True if the response status is anything but a 401 Unauthorized
     */
    public boolean isAuthorizationSuccesful() {
        return !hasFailed() && response.status() != 401;
    }

    /**
     * @return How much the rate limit is on this account {@link DataSiftClient#DEFAULT_NUM} if the information
     *         was not returned
     */
    public int rateLimit() {
        List<String> limit = response.headers().get("X-RateLimit-Limit");
        return limit == null || limit.size() == 0 ? DataSiftClient.DEFAULT_NUM : Integer.parseInt(limit.get(0));
    }

    /**
     * @return How much is left of the rate limit quota or
     *         {@link DataSiftClient#DEFAULT_NUM} if the information was not returned
     */
    public int rateLimitRemaining() {
        List<String> limit = response.headers().get("X-RateLimit-Remaining");
        return limit == null || limit.size() == 0 ? DataSiftClient.DEFAULT_NUM : Integer.parseInt(limit.get(0));
    }

    /**
     * Not all API calls are created equally.
     *
     * @return This tells you how much of your rate limit it took to generate this result or
     *         {@link DataSiftClient#DEFAULT_NUM} if the information was not returned
     */
    public int rateLimitCost() {
        List<String> limit = response.headers().get("X-RateLimit-Cost");
        return limit == null || limit.size() == 0 ? DataSiftClient.DEFAULT_NUM : Integer.parseInt(limit.get(0));
    }

    /**
     * @return if {@link #isSuccessful()} == false then this returns the error message DataSift returned or null if true
     */
    public String getError() {
        return error;
    }

    @Override
    public String toString() {
        return response.toString();
    }

    public void failed(Throwable e) {
        failed = e == null ? false : true;
        cause = e;
    }

    public void successful() {
        failed = false;
    }
}
