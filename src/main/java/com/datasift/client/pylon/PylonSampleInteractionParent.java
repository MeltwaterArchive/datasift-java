package com.datasift.client.pylon;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PylonSampleInteractionParent {
    @JsonProperty
    protected String subtype;
    @JsonProperty
    protected String content;

    public PylonSampleInteractionParent() { }

    public String getSubtype() { return this.subtype; }

    public String getContent() { return this.content; }
}
