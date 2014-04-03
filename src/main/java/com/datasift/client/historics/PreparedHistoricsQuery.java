package com.datasift.client.historics;

import com.datasift.client.BaseDataSiftResult;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class PreparedHistoricsQuery extends BaseDataSiftResult {
    @JsonProperty
    protected double dpus;
    @JsonProperty
    protected String id;
    @JsonProperty
    protected Availability availability;

    /**
     * Create a PreparedHistorics instance containing only an ID
     *
     * @param id the id obtained from DataSift from calling push/create i.e.
     *           {@link DataSiftHistorics#prepare(String, long, long, String, int, String...)}
     * @return an instance which can be used by the client
     */
    public static PreparedHistoricsQuery fromString(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Cannot create a prepared historic from an empty or null string");
        }
        PreparedHistoricsQuery historic = new PreparedHistoricsQuery();
        historic.id = id;
        return historic;
    }

    public double getDpus() {
        return dpus;
    }

    public String getId() {
        return id;
    }

    public Availability getAvailability() {
        return availability;
    }

    protected void id(String id) {
        this.id = id;
    }

    public static class Availability {
        @JsonProperty
        protected long start;
        @JsonProperty
        protected long end;
        @JsonProperty
        protected Map<String, Source> sources;

        public long getStart() {
            return start;
        }

        public long getEnd() {
            return end;
        }

        public void setStart(long start) {
            this.start = start;
        }

        public void setEnd(long end) {
            this.end = end;
        }

        public void setSources(Map<String, Source> sources) {
            this.sources = sources;
        }

        public Map<String, Source> getSources() {
            return sources;
        }
    }

    public static class Source {
        @JsonProperty
        protected long status;
        @JsonProperty
        protected List<Integer> versions;
        @JsonProperty
        protected Map<String, Integer> augmentations;

        public long getStatus() {
            return status;
        }

        public List<Integer> getVersions() {
            return versions;
        }

        public Map<String, Integer> getAugmentations() {
            return augmentations;
        }

        public void setStatus(long status) {
            this.status = status;
        }

        public void setVersions(List<Integer> versions) {
            this.versions = versions;
        }

        public void setAugmentations(Map<String, Integer> augmentations) {
            this.augmentations = augmentations;
        }
    }
}
