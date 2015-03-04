package com.datasift.client.pylon;

import com.datasift.client.BaseDataSiftResult;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PylonQuery extends BaseDataSiftResult {
    @JsonProperty
    protected String hash;
    @JsonProperty
    protected PylonQueryParameters parameters;
    @JsonProperty
    protected String filter;
    @JsonProperty
    protected Integer start;
    @JsonProperty
    protected Integer end;

    public PylonQuery(String hash, PylonQueryParameters parameters, String filter, Integer start, Integer end) {
        this.hash = hash;
        this.parameters = parameters;
        this.filter = filter;
        this.start = start;
        this.end = end;
    }

    public String getHash() { return this.hash; }

    public PylonQueryParameters getParameters() { return this.parameters; }

    public String getFilter() { return this.filter; }

    public Integer getStart() { return this.start; }

    public Integer getEnd() { return this.end; }
}
