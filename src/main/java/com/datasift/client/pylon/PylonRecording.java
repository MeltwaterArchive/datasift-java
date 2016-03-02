package com.datasift.client.pylon;

import com.datasift.client.BaseDataSiftResult;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

public class PylonRecording extends BaseDataSiftResult {
    @JsonIgnore
    protected PylonRecordingId recordingId;

    @JsonProperty
    protected String hash;

    @JsonProperty
    protected int start;

    @JsonProperty
    protected int end;

    @JsonProperty
    protected int volume;

    @JsonProperty
    protected String name;

    @JsonProperty
    protected String status;

    @JsonProperty("remaining_index_capacity")
    protected int remainingIndexCapacity;

    @JsonProperty("remaining_account_capacity")
    protected int remainingAccountCapacity;

    @JsonProperty("reached_capacity")
    protected boolean reachedCapacity;

    public static class PylonRecordingId extends BaseDataSiftResult {

        @JsonProperty
        protected String id;

        public PylonRecordingId() {
        }

        public PylonRecordingId(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

    public PylonRecording() {
    }

    @JsonGetter
    private String getId() { return this.recordingId.id; }

    @JsonSetter
    private void setId(String id) { this.recordingId = new PylonRecordingId(id); }

    public PylonRecordingId getRecordingId() { return this.recordingId; }

    public String getHash() { return this.hash; }

    public int getStart() { return this.start; }

    public int getEnd() { return this.end; }

    public String getName() { return this.name; }

    public int getVolume() { return this.volume; }

    public String getStatus() { return this.status; }

    public int getRemainingIndexCapacity() { return this.remainingIndexCapacity; }

    public int getRemainingAccountCapacity() { return this.remainingAccountCapacity; }

    public boolean isReachedCapacity() { return this.reachedCapacity; }
}
