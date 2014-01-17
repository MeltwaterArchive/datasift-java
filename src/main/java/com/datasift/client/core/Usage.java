package com.datasift.client.core;

import com.datasift.client.DataSiftResult;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

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
    private UsageStream streams;

    public DateTime getStart() {
        return new DateTime(start);
    }

    public DateTime getEnd() {
        return new DateTime(end);
    }

    /**
     * @return A map whose keys are stream hashes and values are a break down of the license usages
     */
    public UsageStream getStreams() {
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

        public void setLicenses(Map<String, Integer> licenses) {
            this.licenses = licenses;
        }

        public void setSeconds(int seconds) {
            this.seconds = seconds;
        }
    }
}
