package com.datasift.client.pylon;

import com.datasift.client.BaseDataSiftResult;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PylonQuery extends BaseDataSiftResult {
    @JsonProperty
    protected String hash;
    @JsonProperty
    protected PylonQueryParameters parameters;
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    protected String filter;
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    protected Integer start;
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    protected Integer end;

    public PylonQuery() { }

    public PylonQuery(String hash, PylonQueryParameters parameters, String filter, Integer start, Integer end) {
        this.hash = hash;
        this.parameters = parameters;
        this.filter = filter;
        this.start = start;
        this.end = end;
    }

    public PylonQuery(String hash, PylonQueryParameters parameters, String filter) {
        new PylonQuery(hash, parameters, filter, null, null);
    }

    public PylonQuery(String hash, PylonQueryParameters parameters) {
        new PylonQuery(hash, parameters, null, null, null);
    }

    public String getHash() { return this.hash; }

    public PylonQueryParameters getParameters() { return this.parameters; }

    public String getFilter() { return this.filter; }

    public Integer getStart() { return this.start; }

    public Integer getEnd() { return this.end; }
}
