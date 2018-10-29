package com.datasift.client.core;

import com.datasift.client.BaseDataSiftResult;
import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class Validation extends BaseDataSiftResult {
    @JsonProperty("created_at")
    protected String createdAt;
    @JsonProperty
    protected float dpu;

    public Validation() {
    }

    /*
     * @return true if the CSDL that was used in the request was checked and found to be valid. False otherwise.
     */
    public boolean isValid() {
        return response.status() == 200;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public float getDpu() {
        return dpu;
    }
}
