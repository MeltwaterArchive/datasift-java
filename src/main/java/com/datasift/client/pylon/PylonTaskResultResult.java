package com.datasift.client.pylon;

import com.datasift.client.BaseDataSiftResult;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PylonTaskResultResult {
    @JsonProperty("analysis")
    protected PylonTaskAnalysisResult analysis;

    @JsonProperty
    protected Long interactions;

    @JsonProperty("unique_authors")
    protected Long uniqueAuthors;

    public PylonTaskResultResult() {
    }

    public PylonTaskAnalysisResult getAnalysis() {
        return this.analysis;
    }

    public Long getInteractions() {
        return this.interactions;
    }

    public Long getUniqueAuthors() {
        return this.uniqueAuthors;
    }

}
