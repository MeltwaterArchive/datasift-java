package com.datasift.client;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class DataSiftAPIResult implements DataSiftResult, APIRateLimit {
    @JsonProperty
    protected String error;
    protected Response response;
    private Throwable cause;
    private boolean failed;

    @Override
    public boolean isSuccessful() {
        return !failed
                && response != null
                && error == null
                && response.status() > 199
                && response.status() < 400;
    }

    @Override
    public Response getResponse() {
        return response;
    }

    @Override
    public void setResponse(Response response) {
        this.response = response;
        //setResponse can happen after a result has been marked as failed
        //so if it is already failed then keep it as a failure
        failed = failed ? failed : response.hasFailed();
        //likewise if already failed keep the existing cause
        cause = cause != null ? cause : response.failureCause();
    }

    @Override
    public Throwable failureCause() {
        return cause;
    }

    @Override
    public boolean isAuthorizationSuccessful() {
        return isSuccessful() && response.status() != 401;
    }

    @Override
    public int rateLimit() {
        List<String> limit = response.headers().get("X-RateLimit-Limit");
        return limit == null || limit.size() == 0 ? DataSiftClient.DEFAULT_NUM : Integer.parseInt(limit.get(0));
    }

    @Override
    public int rateLimitRemaining() {
        List<String> limit = response.headers().get("X-RateLimit-Remaining");
        return limit == null || limit.size() == 0 ? DataSiftClient.DEFAULT_NUM : Integer.parseInt(limit.get(0));
    }

    @Override
    public int rateLimitCost() {
        List<String> limit = response.headers().get("X-RateLimit-Cost");
        return limit == null || limit.size() == 0 ? DataSiftClient.DEFAULT_NUM : Integer.parseInt(limit.get(0));
    }

    @Override
    public String getError() {
        return error;
    }

    @Override
    public String toString() {
        if (response == null) {
            return "{}";
        }
        return response.toString();
    }

    @Override
    public void failed(Throwable e) {
        failed = e != null;
        cause = e;
    }

    @Override
    public void successful() {
        failed = false;
    }
}
