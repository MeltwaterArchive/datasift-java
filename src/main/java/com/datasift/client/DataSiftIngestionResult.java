package com.datasift.client;

import java.util.List;

public class DataSiftIngestionResult extends BaseDataSiftResult {

    @Override
    public int rateLimit() {
        List<String> limit = response.headers().get("X-Ingestion-Request-RateLimit-Limit");
        return limit == null || limit.size() == 0 ? DataSiftClient.DEFAULT_NUM : Integer.parseInt(limit.get(0));
    }

    @Override
    public int rateLimitRemaining() {
        List<String> limit = response.headers().get("X-Ingestion-Request-RateLimit-Remaining");
        return limit == null || limit.size() == 0 ? DataSiftClient.DEFAULT_NUM : Integer.parseInt(limit.get(0));
    }

    public int rateLimitReset() {
        List<String> limit = response.headers().get("X-Ingestion-Request-RateLimit-Reset");
        return limit == null || limit.size() == 0 ? DataSiftClient.DEFAULT_NUM : Integer.parseInt(limit.get(0));
    }

    public int rateLimitResetTTL() {
        List<String> limit = response.headers().get("X-Ingestion-Request-RateLimit-Reset-Ttl");
        return limit == null || limit.size() == 0 ? DataSiftClient.DEFAULT_NUM : Integer.parseInt(limit.get(0));
    }

    public int dataRateLimit() {
        List<String> limit = response.headers().get("X-Ingestion-Data-RateLimit-Limit");
        return limit == null || limit.size() == 0 ? DataSiftClient.DEFAULT_NUM : Integer.parseInt(limit.get(0));
    }

    public int dataRateLimitRemaining() {
        List<String> limit = response.headers().get("X-Ingestion-Data-RateLimit-Remaining");
        return limit == null || limit.size() == 0 ? DataSiftClient.DEFAULT_NUM : Integer.parseInt(limit.get(0));
    }

    public int dataRateLimitReset() {
        List<String> limit = response.headers().get("X-Ingestion-Data-RateLimit-Reset");
        return limit == null || limit.size() == 0 ? DataSiftClient.DEFAULT_NUM : Integer.parseInt(limit.get(0));
    }

    public int dataRateLimitResetTTL() {
        List<String> limit = response.headers().get("X-Ingestion-Data-RateLimit-Reset-Ttl");
        return limit == null || limit.size() == 0 ? DataSiftClient.DEFAULT_NUM : Integer.parseInt(limit.get(0));
    }
}
