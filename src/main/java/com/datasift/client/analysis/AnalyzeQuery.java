package com.datasift.client.analysis;

import com.datasift.client.BaseDataSiftResult;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class AnalyzeQuery extends BaseDataSiftResult {
    @JsonProperty
    protected String hash;
    @JsonProperty("parameters")
    protected JsonNode aggregatorParameters;
    @JsonProperty
    protected String filter;
    @JsonProperty
    protected int start;
    @JsonProperty
    protected int end;
    @JsonProperty("include_parameters_in_reply")
    protected boolean incParametersInReply;

    public AnalyzeQuery() {
    }
}