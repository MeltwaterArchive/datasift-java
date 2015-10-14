package com.datasift.client.pylon;

import com.datasift.client.APIDataSiftResult;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PylonStream extends APIDataSiftResult {
    @JsonProperty("created_at")
    protected long createdAt;
    @JsonProperty
    protected float dpu;
    @JsonProperty
    protected String hash;

    public PylonStream() {
    }

    /**
     * Create a stream instance containing only a hash
     *
     * @param str the hash obtained from DataSift for a stream
     * @return an instance which can be used by the client
     */
    public static PylonStream fromString(String str) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException("Cannot create a stream from an empty or null string");
        }
        PylonStream stream = new PylonStream();
        stream.hash = str;
        return stream;
    }

    public long getCreatedAt() {
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

        PylonStream stream = (PylonStream) o;

        if (Float.compare(stream.dpu, dpu) != 0) {
            return false;
        }
        if (Long.compare(createdAt, stream.createdAt) != 0) {
            return false;
        }
        if (hash != null ? !hash.equals(stream.hash) : stream.hash != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = Long.valueOf(createdAt).hashCode();
        result = 31 * result + (dpu != +0.0f ? Float.floatToIntBits(dpu) : 0);
        result = 31 * result + (hash != null ? hash.hashCode() : 0);
        return result;
    }

    public boolean isSameAs(String hash) {
        return this.hash.equals(hash);
    }

    public boolean isSameAs(PylonStream hash) {
        return this.hash.equals(hash.hash);
    }
}
