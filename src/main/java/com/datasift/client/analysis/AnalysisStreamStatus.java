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
    protected String status;
    @JsonProperty("remaining_capacity")
    protected int remainingCapacity;
    @JsonProperty("reached_capacity")
    protected boolean reachedCapacity;

    public AnalysisStreamStatus() {
    }
}