package com.datasift.client.analysis;

import com.datasift.client.BaseDataSiftResult;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AnalyzeResultAnalysis {
    @JsonProperty("analysis_type")
    protected String analysisType;
    @JsonProperty
    protected AnalysisParametersData parameters;
    @JsonProperty("result")
    protected AnalyzeResultEntryList resultList;

    public String getAnalysisType() { return this.analysisType; }

    public AnalysisParametersData getParameters() { return this.parameters; }

    public AnalyzeResultEntryList getResults() { return this.resultList; }
}
