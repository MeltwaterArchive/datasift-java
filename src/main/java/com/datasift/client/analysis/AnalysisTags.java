package com.datasift.client.analysis;

import com.datasift.client.BaseDataSiftResult;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class AnalysisTags extends BaseDataSiftResult {
    @JsonProperty
    protected final List<String> tags = new ArrayList<String>();

    public AnalysisTags() { }

    @JsonCreator
    public AnalysisTags(@JsonProperty List<String> tags) {
        if (tags != null) {
            this.tags.addAll(tags);
        }
    }

    public List<String> getTags() { return this.tags; }
}