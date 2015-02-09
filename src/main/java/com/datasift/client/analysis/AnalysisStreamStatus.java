package com.datasift.client.analysis;

import com.datasift.client.BaseDataSiftResult;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AnalysisStreamStatus extends BaseDataSiftResult {
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

    public AnalysisStreamStatus() {
    }

    public String getHash() { return this.hash; }

    public int getStart() { return this.start; }

    public int getEnd() { return this.end; }

    public int getVolume() { return this.volume; }

    public String getStatus() { return this.status; }

    public int getRemainingIndexCapacity() { return this.remainingIndexCapacity; }

    public int getRemainingAccountCapacity() { return this.remainingAccountCapacity; }

    public boolean getReachedCapacity() { return this.reachedCapacity; }
}
