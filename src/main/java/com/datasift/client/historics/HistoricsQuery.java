package com.datasift.client.historics;

import com.datasift.client.DataSiftResult;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class HistoricsQuery extends DataSiftResult {
    @JsonProperty
    private String id;
    @JsonProperty("definition_id")
    private String definitationId;
    @JsonProperty
    private String name;
    @JsonProperty
    private long start;
    @JsonProperty
    private long end;
    @JsonProperty("created_at")
    private long createdAt;
    @JsonProperty
    private String status;
    @JsonProperty
    private double progress;
    @JsonProperty
    private List<String> feed;
    @JsonProperty
    private List<String> sources;
    @JsonProperty
    private double sample;
    @JsonProperty
    private List<Chunk> chunks;

    public String getId() {
        return id;
    }

    public String getDefinitationId() {
        return definitationId;
    }

    public String getName() {
        return name;
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public String getStatus() {
        return status;
    }

    public double getProgress() {
        return progress;
    }

    public List<String> getFeed() {
        return feed == null ? new ArrayList<String>() : feed;
    }

    public List<String> getSources() {
        return sources == null ? new ArrayList<String>() : sources;
    }

    public double getSample() {
        return sample;
    }

    /**
     * @return a list of chunks in this historic query or an empty list. will never return null
     */
    public List<Chunk> getChunks() {
        return chunks == null ? new ArrayList<Chunk>() : chunks;
    }

    public static class Chunk {
        @JsonProperty
        private String status;
        @JsonProperty
        private double progress;
        @JsonProperty("start_time")
        private long startTime;
        @JsonProperty("end_time")
        private long endTime;
        @JsonProperty("estimated_completion")
        private long estimatedCompletion;

        public String getStatus() {
            return status;
        }

        public double getProgress() {
            return progress;
        }

        public DateTime getStartTime() {
            return new DateTime(TimeUnit.SECONDS.toMillis(startTime));
        }

        public DateTime getEndTime() {
            return new DateTime(TimeUnit.SECONDS.toMillis(endTime));
        }

        public DateTime getEstimatedCompletion() {
            return new DateTime(TimeUnit.SECONDS.toMillis(estimatedCompletion));
        }
    }
}
