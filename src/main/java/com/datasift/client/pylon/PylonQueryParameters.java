package com.datasift.client.pylon;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PylonQueryParameters {
    @JsonProperty("analysis_type")
    protected String analysisType;
    @JsonProperty
    protected PylonParametersData parameters;
    @JsonProperty("child")
    protected PylonQueryParameters childAnalysis;

    public PylonQueryParameters() {
    }

    public PylonQueryParameters(String analysisType, PylonParametersData parameters, PylonQueryParameters child) {
        this.analysisType = analysisType;
        this.parameters = parameters;
        this.childAnalysis = child;
    }

    public String getAnalysisType() {
        return this.analysisType;
    }

    public PylonParametersData getParameters() {
        return this.parameters;
    }

    public PylonQueryParameters getChildAnalysis() { return this.childAnalysis; }

    public void setAnalysisType(String analysisType) {
        this.analysisType = analysisType;
    }

    public void setParameters(PylonParametersData parameters) {
        this.parameters = parameters;
    }

    public void setChildAnalysis(PylonQueryParameters childAnalysis) { this.childAnalysis = childAnalysis; }
}
