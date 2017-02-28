package com.datasift.client.pylon;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class PylonTaskAnalysisResultBucket {
    @JsonProperty
    protected long interactions;

    @JsonProperty("unique_authors")
    protected long uniqueAuthors;

    @JsonProperty
    protected String key;

    public PylonTaskAnalysisResultBucket() {
    }

    public long getInteractions() {
        return this.interactions;
    }

    public long getUniqueAuthors() {
        return this.uniqueAuthors;
    }

    public String getKey() {
        return this.key;
    }
}
