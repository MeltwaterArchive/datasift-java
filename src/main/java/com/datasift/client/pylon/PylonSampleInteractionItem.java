package com.datasift.client.pylon;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PylonSampleInteractionItem {
    @JsonProperty("interaction")
    protected PylonSampleInteractionParent interactionParent;
    @JsonProperty("fb")
    protected PylonSampleInteraction interaction;

    public PylonSampleInteractionItem() { }

    public PylonSampleInteractionParent getInteractionParent() { return this.interactionParent; }

    public PylonSampleInteraction getInteraction() { return this.interaction; }
}
