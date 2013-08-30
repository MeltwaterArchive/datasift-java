package com.datasift.client.stream;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

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
    @JsonProperty
    private List<String> hashes;

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

    /**
     * @return true if the object is a DataSift message and has a set of hashes that the message relates to
     */
    public boolean hasHashes() {
        return isDataSiftMessage() && hashes != null && hashes.size() > 0;
    }

    /**
     * @return A set of hashes this message is related to. Will never return null but can be empty
     */
    public List<? extends String> hashes() {
        return hashes == null ? new ArrayList<String>() : hashes;
    }
}
