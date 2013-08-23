package com.datasift.client.core;

import com.datasift.client.DataSiftResult;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.Map;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class Usage extends DataSiftResult {
    @JsonProperty
    private Date start;
    @JsonProperty
    private Date end;
    @JsonProperty
    private Map<String, UsageStream> streams;

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    /**
     * @return A map whose keys are stream hashes and values are a break down of the license usages
     */
    public Map<String, UsageStream> getStreams() {
        return streams;
    }

    public static enum Period {DAY, HOUR, CURRENT}

    public static class UsageStream {
        @JsonProperty
        private Map<String, Integer> licenses;
        @JsonProperty
        private int seconds;

        public Map<String, Integer> getLicenses() {
            return licenses;
        }

        public int getSeconds() {
            return seconds;
        }
    }
}
