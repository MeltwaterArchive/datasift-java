package com.datasift.client.core;

import com.datasift.client.DataSiftResult;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

import java.util.Date;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class Validation extends DataSiftResult {
    @JsonProperty("created_at")
    protected Date createdAt;
    @JsonProperty
    protected float dpu;

    public Validation() {
    }

    /**
     * @return true if the CSDL that was used in the request was checked and found to be valid. False otherwise.
     */
    public boolean isValid() {
        return response.status() == 200;
    }

    public DateTime getCreatedAt() {
        return new DateTime(createdAt);
    }

    public float getDpu() {
        return dpu;
    }
}
