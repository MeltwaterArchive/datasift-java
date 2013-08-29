package com.datasift.client.stream;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class MultiStreamInteraction {
    @JsonProperty
    protected String hash;
    @JsonProperty
    protected JsonNode data;
    @JsonProperty
    protected String status;
    @JsonProperty
    protected String message;
    @JsonProperty
    private long tick;

    /**
     * DataSift specific messages always have a status and message field.
     * If these are null then this returns false otherwise true
     */
    public boolean isDataSiftMessage() {
        return status != null && message != null;
    }

    public String getHash() {
        return hash;
    }

    public JsonNode getData() {
        return data;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public long getTick() {
        return tick;
    }
}
