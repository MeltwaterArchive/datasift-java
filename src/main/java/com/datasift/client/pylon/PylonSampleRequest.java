package com.datasift.client.pylon;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PylonSampleRequest {
    @JsonProperty
    protected String hash;
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    protected int count;
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    protected String filter;
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    protected int start;
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    protected int end;

    public PylonSampleRequest() { }

    public PylonSampleRequest(String hash, Integer count, String filter, Integer start, Integer end) {
        this.hash = hash;
        this.filter = filter;
        if (count != null) { this.count = count; }
        if (start != null) { this.start = start; }
        if (end != null) { this.end = end; }
    }

    public PylonSampleRequest(String hash, Integer count, String filter) { this(hash, count, filter, null, null); }

    public PylonSampleRequest(String hash, Integer count) {
        this(hash, count, null, null, null);
    }

    public PylonSampleRequest(String hash, String filter) { this(hash, null, filter, null, null); }

    public PylonSampleRequest(String hash) { this(hash, null, null, null, null); }

    public String getHash() { return this.hash; }

    public Integer getCount() { return this.count; }

    public String getFilter() { return this.filter; }

    public int getStart() { return this.start; }

    public int getEnd() { return this.end; }
}
