package com.datasift.client.pylon;

import com.datasift.client.DataSiftAPIResult;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PylonResult extends DataSiftAPIResult {
    @JsonProperty
    protected int interactions;
    @JsonProperty("unique_authors")
    protected int uniqueAuthors;
    @JsonProperty
    protected PylonResultAnalysis analysis;

    public PylonResult() { }

    public int getInteractions() { return this.interactions; }

    public int getUniqueAuthors() { return this.uniqueAuthors; }

    public PylonResultAnalysis getAnalysis() { return this.analysis; }
}
