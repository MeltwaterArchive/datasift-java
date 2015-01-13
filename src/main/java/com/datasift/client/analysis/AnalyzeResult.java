package com.datasift.client.analysis;

import com.datasift.client.BaseDataSiftResult;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AnalyzeResult extends BaseDataSiftResult {
    @JsonProperty
    protected boolean truncated;
    @JsonProperty
    protected int interactions;
    @JsonProperty("unique_authors")
    protected int uniqueAuthors;
    @JsonProperty
    protected List<BaseDataSiftResult> results;

    public AnalyzeResult() {
    }

    public boolean getTruncated() { return this.truncated; }

    public int getInteractions() { return this.interactions; }

    public int getUniqueAuthors() { return this.uniqueAuthors; }

    public List<BaseDataSiftResult> getResults() { return this.results; }
}