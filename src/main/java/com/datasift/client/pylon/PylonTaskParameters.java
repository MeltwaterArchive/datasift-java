package com.datasift.client.pylon;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PylonTaskParameters {
    @JsonProperty
    protected PylonQueryParameters parameters;

    @JsonProperty("child")
    protected PylonTaskParametersChild childAnalysis;

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    protected int start;

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    protected int end;

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    protected String filter;

    public PylonTaskParameters() {
    }

    public PylonTaskParameters(PylonQueryParameters parameters, Integer start, Integer end) {
        this.parameters = parameters;
        if (start != null) {
            this.start = start;
        }
        if (end != null) {
            this.end = end;
        }
    }

    public PylonTaskParameters(PylonQueryParameters parameters, Integer start, Integer end, String filter) {
        this.parameters = parameters;
        if (start != null) {
            this.start = start;
        }
        if (end != null) {
            this.end = end;
        }
        if (filter != null) {
            this.filter = filter;
        }
    }

    public PylonTaskParameters(String analysisType,
                               PylonQueryParameters parameters,
                               PylonTaskParametersChild child,
                               Integer start,
                               Integer end) {
        this.parameters = parameters;
        this.childAnalysis = child;
        if (start != null) {
            this.start = start;
        }
        if (end != null) {
            this.end = end;
        }
    }

    public PylonQueryParameters getParameters() {
        return this.parameters;
    }

    public PylonTaskParametersChild getChildAnalysis() {
        return this.childAnalysis;
    }

    public void setParameters(PylonQueryParameters parameters) {
        this.parameters = parameters;
    }

    public void setChildAnalysis(PylonTaskParametersChild childAnalysis) {
        this.childAnalysis = childAnalysis;
    }

    public int getStart() {
        return this.start;
    }

    public int getEnd() {
        return this.end;
    }

    public String getFilter() {
        return this.filter;
    }
}
