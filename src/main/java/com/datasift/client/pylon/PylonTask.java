package com.datasift.client.pylon;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PylonTask {
    @JsonProperty("subscription_id")
    protected String subscriptionId;

    @JsonProperty
    protected PylonTaskParameters parameters;

    @JsonProperty
    protected String type;

    @JsonProperty("created_at")
    protected long createdAt;

    @JsonProperty("updated_at")
    protected long updatedAt;

    @JsonProperty("id")
    protected String id;

    @JsonProperty("id")
    protected String status;

    public PylonTask() {
    }

    public PylonTask(String subscriptionId,
                     PylonTaskParameters parameters,
                     String type) {
        this.subscriptionId = subscriptionId;
        this.parameters = parameters;
        this.type = type;
    }

    public String getRecordingId() {
        return this.subscriptionId;
    }

    public PylonTaskParameters getParameters() {
        return this.parameters;
    }

    public String getType() {
        return this.type;
    }

    public long getCreatedAt() {
        return this.createdAt;
    }

    public long getUpdatedAt() {
        return this.updatedAt;
    }

    public String getId() {
        return this.id;
    }

    public String getStatus() {
        return this.status;
    }

}
