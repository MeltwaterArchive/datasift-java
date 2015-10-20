package com.datasift.client;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class DataSiftIngestionResult implements DataSiftResult, IngestionRateLimit {
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
    public int requestRateLimit() {
        List<String> limit = response.headers().get("X-Ingestion-Request-RateLimit-Limit");
        return limit == null || limit.size() == 0 ? DataSiftClient.DEFAULT_NUM : Integer.parseInt(limit.get(0));
    }

    @Override
    public int requestRateLimitRemaining() {
        List<String> limit = response.headers().get("X-Ingestion-Request-RateLimit-Remaining");
        return limit == null || limit.size() == 0 ? DataSiftClient.DEFAULT_NUM : Integer.parseInt(limit.get(0));
    }

    @Override
    public int requestRateLimitReset() {
        List<String> limit = response.headers().get("X-Ingestion-Request-RateLimit-Reset");
        return limit == null || limit.size() == 0 ? DataSiftClient.DEFAULT_NUM : Integer.parseInt(limit.get(0));
    }

    @Override
    public int requestRateLimitResetTTL() {
        List<String> limit = response.headers().get("X-Ingestion-Request-RateLimit-Reset-Ttl");
        return limit == null || limit.size() == 0 ? DataSiftClient.DEFAULT_NUM : Integer.parseInt(limit.get(0));
    }

    @Override
    public int dataRateLimit() {
        List<String> limit = response.headers().get("X-Ingestion-Data-RateLimit-Limit");
        return limit == null || limit.size() == 0 ? DataSiftClient.DEFAULT_NUM : Integer.parseInt(limit.get(0));
    }

    @Override
    public int dataRateLimitRemaining() {
        List<String> limit = response.headers().get("X-Ingestion-Data-RateLimit-Remaining");
        return limit == null || limit.size() == 0 ? DataSiftClient.DEFAULT_NUM : Integer.parseInt(limit.get(0));
    }

    @Override
    public int dataRateLimitReset() {
        List<String> limit = response.headers().get("X-Ingestion-Data-RateLimit-Reset");
        return limit == null || limit.size() == 0 ? DataSiftClient.DEFAULT_NUM : Integer.parseInt(limit.get(0));
    }

    @Override
    public int dataRateLimitResetTTL() {
        List<String> limit = response.headers().get("X-Ingestion-Data-RateLimit-Reset-Ttl");
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
