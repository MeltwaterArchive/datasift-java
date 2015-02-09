package com.datasift.client.analysis;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AnalysisQueryParameters {
    @JsonProperty("analysis_type")
    protected String analysisType;
    @JsonProperty
    protected AnalysisParametersData parameters;

    public AnalysisQueryParameters() {
    }

    public AnalysisQueryParameters(String analysisType, AnalysisParametersData parameters) {
        this.analysisType = analysisType;
        this.parameters = parameters;
    }

    public String getAnalysisType() {
        return this.analysisType;
    }

    public AnalysisParametersData getParameters() {
        return this.parameters;
    }

    public void setAnalysisType(String analysisType) {
        this.analysisType = analysisType;
    }

    public void setParameters(AnalysisParametersData parameters) {
        this.parameters = parameters;
    }
}
