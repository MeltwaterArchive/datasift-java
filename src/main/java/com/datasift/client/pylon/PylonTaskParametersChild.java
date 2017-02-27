package com.datasift.client.pylon;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PylonTaskParametersChild {
    @JsonProperty("analysis_type")
    protected String analysisType;
    @JsonProperty
    protected PylonParametersData parameters;
    @JsonProperty("child")
    protected PylonTaskParametersChild childAnalysis;

    public PylonTaskParametersChild() {
    }

    public PylonTaskParametersChild(String analysisType, PylonParametersData parameters, Integer start, Integer end) {
        this.analysisType = analysisType;
        this.parameters = parameters;
    }

    public PylonTaskParametersChild(String analysisType,
                                    PylonParametersData parameters,
                                    PylonTaskParametersChild child,
                                    Integer start,
                                    Integer end) {
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

    public PylonTaskParametersChild getChildAnalysis() {
        return this.childAnalysis;
    }

    public void setAnalysisType(String analysisType) {
        this.analysisType = analysisType;
    }

    public void setParameters(PylonParametersData parameters) {
        this.parameters = parameters;
    }

    public void setChildAnalysis(PylonTaskParametersChild childAnalysis) {
        this.childAnalysis = childAnalysis;
    }

}
