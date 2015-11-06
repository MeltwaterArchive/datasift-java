package com.datasift.client.pylon;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PylonQuery {
    @JsonProperty
    protected String hash;
    @JsonProperty
    protected PylonQueryParameters parameters;
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    protected String filter;
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    protected int start;
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    protected int end;

    public PylonQuery() { }

    public PylonQuery(String hash, PylonQueryParameters parameters, String filter, Integer start, Integer end) {
        this.hash = hash;
        this.parameters = parameters;
        this.filter = filter;
        if (start != null) { this.start = start; }
        if (end != null) { this.end = end; }
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

    public int getStart() { return this.start; }

    public int getEnd() { return this.end; }
}
