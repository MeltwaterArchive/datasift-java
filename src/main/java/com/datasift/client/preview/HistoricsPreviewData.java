package com.datasift.client.preview;

import com.datasift.client.DataSiftResult;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class HistoricsPreviewData extends DataSiftResult {
    @JsonProperty
    protected String id;
    @JsonProperty
    protected String name;
    @JsonProperty
    protected int progress;
    @JsonProperty
    protected String status;
    @JsonProperty
    protected String feeds;
    @JsonProperty
    protected int sample;
    @JsonProperty
    protected long start;
    @JsonProperty
    protected long end;
    @JsonProperty
    protected long created_at;
    @JsonProperty
    protected String user;
    @JsonProperty
    protected String parameters;
    @JsonProperty
    protected String hash;
    @JsonProperty
    protected List<DataEntry> data;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getProgress() {
        return progress;
    }

    public String getStatus() {
        return status;
    }

    public String getFeeds() {
        return feeds;
    }

    public int getSample() {
        return sample;
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }

    public long getCreated_at() {
        return created_at;
    }

    public String getUser() {
        return user;
    }

    public String getParameters() {
        return parameters;
    }

    public String getHash() {
        return hash;
    }

    public List<DataEntry> getData() {
        return data;
    }

    public static class DataEntry {
        @JsonProperty
        protected String target;
        @JsonProperty
        protected String analysis;
        @JsonProperty
        protected Map<String, Long> output;
        @JsonProperty
        protected int threshold;

        public String getTarget() {
            return target;
        }

        public String getAnalysis() {
            return analysis;
        }

        public Map<String, Long> getOutput() {
            return output;
        }

        public int getThreshold() {
            return threshold;
        }
    }
}
