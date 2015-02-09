package com.datasift.client.analysis;

import com.datasift.client.BaseDataSiftResult;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AnalysisStream extends BaseDataSiftResult {
    @JsonProperty("created_at")
    protected long createdAt;
    @JsonProperty
    protected float dpu;
    @JsonProperty
    protected String hash;

    public AnalysisStream() {
    }

    /**
     * Create a stream instance containing only a hash
     *
     * @param str the hash obtained from DataSift for a stream
     * @return an instance which can be used by the client
     */
    public static AnalysisStream fromString(String str) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException("Cannot create a stream from an empty or null string");
        }
        AnalysisStream stream = new AnalysisStream();
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

        AnalysisStream stream = (AnalysisStream) o;

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

    public boolean isSameAs(AnalysisStream hash) {
        return this.hash.equals(hash.hash);
    }
}
