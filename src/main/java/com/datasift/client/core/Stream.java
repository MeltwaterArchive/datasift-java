package com.datasift.client.core;

import com.datasift.client.DataSiftResult;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

import java.util.Date;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class Stream extends DataSiftResult {
    @JsonProperty("created_at")
    protected Date createdAt;
    @JsonProperty
    protected float dpu;
    @JsonProperty
    protected float hash;

    public float getHash() {
        return hash;
    }

    public DateTime getCreatedAt() {
        return new DateTime(createdAt);
    }

    public float getDpu() {
        return dpu;
    }

}
