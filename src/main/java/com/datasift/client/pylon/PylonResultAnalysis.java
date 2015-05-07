package com.datasift.client.pylon;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PylonResultAnalysis {
    @JsonProperty("analysis_type")
    protected String analysisType;
    @JsonProperty
    protected PylonParametersData parameters;
    @JsonProperty("result")
    protected PylonResultEntryList resultList;

    public String getAnalysisType() { return this.analysisType; }

    public PylonParametersData getParameters() { return this.parameters; }

    public PylonResultEntryList getResults() { return this.resultList; }
}
