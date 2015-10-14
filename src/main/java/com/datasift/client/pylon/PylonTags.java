package com.datasift.client.pylon;

import com.datasift.client.DataSiftAPIResult;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class PylonTags extends DataSiftAPIResult {
    @JsonProperty
    protected final List<String> tags = new ArrayList<String>();

    public PylonTags() { }

    @JsonCreator
    public PylonTags(List<String> tags) {
        if (tags != null) {
            this.tags.addAll(tags);
        }
    }

    public List<String> getTags() { return this.tags; }
}
