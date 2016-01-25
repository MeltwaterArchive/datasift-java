package com.datasift.client.pylon;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class PylonSampleInteraction {
    @JsonProperty("media_type")
    protected String mediaType;
    @JsonProperty
    protected String content;
    @JsonProperty
    protected String language;
    @JsonProperty("topic_ids")
    protected List<Long> topicIDs = new ArrayList<>();

    public PylonSampleInteraction() { }

    public String getMediaType() { return this.mediaType; }

    public String getContent() { return this.content; }

    public String getLanguage() { return this.language; }

    public List<Long> getTopicIDs() { return this.topicIDs; }
}
