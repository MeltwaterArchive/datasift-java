package com.datasift.client.analysis;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AnalyzeResultEntry {
    @JsonProperty
    protected String key;
    @JsonProperty
    protected int interactions;
    @JsonProperty("unique_authors")
    protected int uniqueAuthors;

    public String getKey() { return this.key; }

    public int getInteractions() { return this.interactions; }

    public int getUniqueAuthors() { return this.uniqueAuthors; }
}
