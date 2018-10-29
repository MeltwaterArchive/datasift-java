package com.datasift.client.core;

import com.datasift.client.BaseDataSiftResult;
import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class Stream extends BaseDataSiftResult {
    @JsonProperty("created_at")
    protected String createdAt;
    @JsonProperty
    protected float dpu;
    @JsonProperty
    protected String hash;

    public Stream() {
    }

    /*
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

    public String getCreatedAt() {
        return createdAt;
    }

    public float getDpu() {
        return dpu;
    }

    @JsonProperty
    public String hash() {
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Stream stream = (Stream) o;

        if (Float.compare(stream.dpu, dpu) != 0) {
            return false;
        }
        if (createdAt != null ? !createdAt.equals(stream.createdAt) : stream.createdAt != null) {
            return false;
        }
        if (hash != null ? !hash.equals(stream.hash) : stream.hash != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = createdAt != null ? createdAt.hashCode() : 0;
        result = 31 * result + (dpu != +0.0f ? Float.floatToIntBits(dpu) : 0);
        result = 31 * result + (hash != null ? hash.hashCode() : 0);
        return result;
    }

    public boolean isSameAs(String hash) {
        return this.hash.equals(hash);
    }

    public boolean isSameAs(Stream hash) {
        return this.hash.equals(hash.hash);
    }
}
