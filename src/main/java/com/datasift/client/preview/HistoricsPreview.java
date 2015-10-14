package com.datasift.client.preview;

import com.datasift.client.APIDataSiftResult;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class HistoricsPreview extends APIDataSiftResult {
    @JsonProperty("created_at")
    protected String createdAt;
    @JsonProperty
    protected String id;

    protected HistoricsPreview() {
    }

    /**
     * Create a stream instance containing only a hash
     *
     * @param str the hash obtained from DataSift for a stream
     * @return an instance which can be used by the client
     */
    public static HistoricsPreview fromString(String str) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException("Cannot create a preview from an empty or null string");
        }
        HistoricsPreview stream = new HistoricsPreview();
        stream.id = str;
        return stream;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String id() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HistoricsPreview stream = (HistoricsPreview) o;
        if (createdAt != null ? !createdAt.equals(stream.createdAt) : stream.createdAt != null) {
            return false;
        }
        if (id != null ? !id.equals(stream.id) : stream.id != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = createdAt != null ? createdAt.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }

    public boolean isSameAs(String hash) {
        return this.id.equals(hash);
    }

    public boolean isSameAs(HistoricsPreview hash) {
        return this.id.equals(hash.id);
    }
}
