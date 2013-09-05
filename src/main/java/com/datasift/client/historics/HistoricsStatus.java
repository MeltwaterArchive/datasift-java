package com.datasift.client.historics;

import com.datasift.client.DataSiftResult;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class HistoricsStatus extends DataSiftResult {
    @JsonProperty
    private long start;
    @JsonProperty
    private long end;
    @JsonProperty
    private Map<String, DataSource> sources;

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }

    /**
     * @return A map whose keys are the names of the datasource such as "facebook" or "twitter"
     *         and values that tells you the status (%) of data available for each source as well as versions of the
     *         data is available which augmentations and the percent of availability for each
     */
    public Map<String, DataSource> getSources() {
        return sources;
    }

    public static class DataSource {
        @JsonProperty
        private Map<String, Integer> augmentations;
        @JsonProperty
        private List<String> versions;
        @JsonProperty
        private double status;

        /**
         * @return map of augmentations such as "language" => 100 where 100 is the % availability
         */
        public Map<String, Integer> getAugmentations() {
            return augmentations;
        }

        public List<String> getVersions() {
            return versions;
        }

        /**
         * @return the % availability for this data source within the time frame used to generate this result
         */
        public double getStatus() {
            return status;
        }
    }
}
