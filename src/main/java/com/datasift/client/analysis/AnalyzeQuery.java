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
    @JsonProperty
    protected AnalysisQueryParameters parameters;
    @JsonProperty
    protected String filter;
    @JsonProperty
    protected Integer start;
    @JsonProperty
    protected Integer end;

    public AnalyzeQuery(String hash, AnalysisQueryParameters parameters, String filter, Integer start, Integer end) {
        this.hash = hash;
        this.parameters = parameters;
        this.filter = filter;
        this.start = start;
        this.end = end;
    }

    public String getHash() { return this.hash; }

    public AnalysisQueryParameters getParameters() { return this.parameters; }

    public String getFilter() { return this.filter; }

    public Integer getStart() { return this.start; }

    public Integer getEnd() { return this.end; }
}
