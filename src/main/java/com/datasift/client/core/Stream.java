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
    protected String hash;

    protected Stream() {
    }

    /**
     * Create a stream instance containing only a hash
     *
     * @param str the hash obtained from DataSift for a stream
     * @return an instance which can be used by the client
     */
    public static Stream fromString(String str) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException("Cannot create a stream from an empty or null string");
        }
        Stream stream = new Stream();
        stream.hash = str;
        return stream;
    }

    public DateTime getCreatedAt() {
        return new DateTime(createdAt);
    }

    public float getDpu() {
        return dpu;
    }

    @JsonProperty
    public String hash() {
        return hash;
    }
}
