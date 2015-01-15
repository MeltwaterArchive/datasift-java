package com.datasift.client.analysis;

import com.datasift.client.BaseDataSiftResult;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.cliffc.high_scale_lib.NonBlockingHashSet;

import java.util.Map;

public class AnalyzeQuery extends BaseDataSiftResult {
    @JsonProperty
    protected String hash;
    @JsonProperty("parameters")
    protected Map<String, Object> aggregatorParameters = new NonBlockingHashMap<String, Object>();
    @JsonProperty
    protected String filter;
    @JsonProperty
    protected int start;
    @JsonProperty
    protected int end;

    public AnalyzeQuery(String hash, Map<String, Object> parameters, String filter, int start, int end) {
        this.hash = hash;
        this.aggregatorParameters = parameters;
        this.filter = filter;
        this.start = start;
        this.end = end;
    }

    public String getHash() { return this.hash; }

    public Map<String, Object> getAggregatorParameters() { return this.aggregatorParameters; }

    public String getFilter() { return this.filter; }

    public int getStart() { return this.start; }

    public int getEnd() { return this.end; }
}
