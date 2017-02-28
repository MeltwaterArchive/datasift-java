package com.datasift.client.pylon;

import com.datasift.client.BaseDataSiftResult;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PylonTaskResult extends BaseDataSiftResult {

    public PylonTaskResult() {
    }

    @JsonProperty
    protected String id;

    @JsonProperty("identity_id")
    protected String identityId;

    @JsonProperty("subscription_id")
    protected String subscriptionId;

    @JsonProperty
    protected String name;

    @JsonProperty
    protected String type;

    @JsonProperty
    protected PylonTaskParameters parameters;

    @JsonProperty
    protected PylonTaskResultResult result;

    @JsonProperty
    protected String status;

    @JsonProperty("created_at")
    protected int createdAt;

    @JsonProperty("updated_at")
    protected int updatedAt;

    public String getId() {
        return id;
    }

    public String getIdentityId() {
        return identityId;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public PylonTaskParameters getParameters() {
        return parameters;
    }

    public PylonTaskResultResult getResult() {
        return result;
    }

    public String getStatus() {
        return status;
    }

    public int getCreatedAt() {
        return createdAt;
    }

    public int getUpdatedAt() {
        return updatedAt;
    }

}
