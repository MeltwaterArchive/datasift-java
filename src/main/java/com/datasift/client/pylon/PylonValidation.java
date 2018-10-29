package com.datasift.client.pylon;

import com.datasift.client.BaseDataSiftResult;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PylonValidation extends BaseDataSiftResult {
    @JsonProperty("created_at")
    protected long createdAt;
    @JsonProperty
    protected float dpu;

    public PylonValidation() {
    }

    /*
     * @return true if the CSDL that was used in the request was checked and found to be valid. False otherwise.
     */
    public boolean isValid() {
        return response.status() == 200;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public float getDpu() {
        return dpu;
    }
}
