package com.datasift.client.pylon;

import com.datasift.client.pylon.PylonRecording.PylonRecordingId;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

public class PylonTaskRequest {
    @JsonProperty("subscription_id")
    protected String subscriptionId;

    @JsonProperty
    protected PylonTaskParameters parameters;

    @JsonProperty
    protected String name;

    @JsonProperty
    protected String type;

    public PylonTaskRequest() {
    }

    public PylonTaskRequest(String subscriptionId,
                            PylonTaskParameters parameters,
                            String name,
                            String type) {
        this.subscriptionId = subscriptionId;
        this.parameters = parameters;
        this.name = name;
        this.type = type;
    }

    public String getRecordingId() {
        return this.subscriptionId;
    }

    public PylonTaskParameters getParameters() {
        return this.parameters;
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

}
