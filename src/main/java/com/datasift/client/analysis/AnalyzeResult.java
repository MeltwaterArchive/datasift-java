package com.datasift.client.analysis;

import com.datasift.client.BaseDataSiftResult;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AnalyzeResult extends BaseDataSiftResult {
    @JsonProperty
    protected int interactions;
    @JsonProperty("unique_authors")
    protected int uniqueAuthors;
    @JsonProperty
    protected AnalyzeResultAnalysis analysis;

    public AnalyzeResult() { }

    public int getInteractions() { return this.interactions; }

    public int getUniqueAuthors() { return this.uniqueAuthors; }

    public AnalyzeResultAnalysis getAnalysis() { return this.analysis; }
}
