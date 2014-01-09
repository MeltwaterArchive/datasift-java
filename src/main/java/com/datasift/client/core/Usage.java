package com.datasift.client.core;

import com.datasift.client.DataSiftResult;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class Usage extends DataSiftResult {
    @JsonProperty
    private String start;
    @JsonProperty
    private String end;
    @JsonProperty
    private Map<String, UsageStream> streams;

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    /**
     * @return A map whose keys are stream hashes and values are a break down of the license usages
     */
    public Map<String, UsageStream> getStreams() {
        return streams;
    }

    public static enum Period {
        DAY,
        HOUR,
        CURRENT;

        public static Period fromStr(String str) {
            try {
                return Period.valueOf(str.toUpperCase());
            } catch (IllegalArgumentException iae) {
                return Period.valueOf(str);
            }
        }
    }

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
