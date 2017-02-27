package com.datasift.client.pylon;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class PylonTaskAnalysisResult {
    @JsonProperty("analysis_type")
    protected String analysisType;

    @JsonProperty
    protected PylonParametersData parameters;

    @JsonProperty
    protected boolean redacted;

    @JsonProperty
    protected List<PylonTaskAnalysisResultBucket> results = new ArrayList<PylonTaskAnalysisResultBucket>();

    public PylonTaskAnalysisResult() {
    }

    public String getAnalysisType() {
        return this.analysisType;
    }

    public PylonParametersData getParameters() {
        return this.parameters;
    }

    public boolean getRedacted() {
        return this.redacted;
    }

    public List<PylonTaskAnalysisResultBucket> getResults() {
        return this.results;
    }
}
