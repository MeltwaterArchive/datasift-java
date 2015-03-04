package com.datasift.client.pylon;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PylonQueryParameters {
    @JsonProperty("analysis_type")
    protected String analysisType;
    @JsonProperty
    protected PylonParametersData parameters;

    public PylonQueryParameters() {
    }

    public PylonQueryParameters(String analysisType, PylonParametersData parameters) {
        this.analysisType = analysisType;
        this.parameters = parameters;
    }

    public String getAnalysisType() {
        return this.analysisType;
    }

    public PylonParametersData getParameters() {
        return this.parameters;
    }

    public void setAnalysisType(String analysisType) {
        this.analysisType = analysisType;
    }

    public void setParameters(PylonParametersData parameters) {
        this.parameters = parameters;
    }
}
