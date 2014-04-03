package com.datasift.client.push;

import com.datasift.client.BaseDataSiftResult;
import com.datasift.client.push.connectors.BaseConnector;
import com.datasift.client.push.connectors.PushConnector;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class PushSubscription extends BaseDataSiftResult {
    @JsonProperty
    private String id;
    @JsonProperty("output_type")
    private String outputType;
    @JsonProperty
    private String name;
    @JsonProperty("created_at")
    private long createdAt;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("hash")
    private String hash;
    @JsonProperty("hash_type")
    private String hashType;
    @JsonProperty("output_params")
    private Map<String, Object> outputParams;
    @JsonProperty
    private Status status;
    @JsonProperty("last_request")
    private long lastRequest;
    @JsonProperty("last_success")
    private long lastSuccess;
    @JsonProperty("remaining_bytes")
    private long remainingBytes;
    @JsonProperty
    private boolean lostData;
    @JsonProperty
    private long start;
    @JsonProperty
    private long end;

    public String getId() {
        return id;
    }

    public <T extends PushConnector> OutputType<T> getOutputType() {
        return OutputType.fromString(outputType);
    }

    public String name() {
        return name;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public String getUserId() {
        return userId;
    }

    public String hash() {
        return hash;
    }

    public String getHashType() {
        return hashType;
    }

    public PushConnector getOutputParams() {
        return BaseConnector.fromMap(getOutputType(), outputParams);
    }

    public Status status() {
        return status;
    }

    public long getLastRequest() {
        return lastRequest;
    }

    public long getLastSuccess() {
        return lastSuccess;
    }

    public long getRemainingBytes() {
        return remainingBytes;
    }

    public boolean isLostData() {
        return lostData;
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }

    /**
     * Create a {@link PushSubscription} instance containing only an id
     *
     * @param str the ID obtained from DataSift for creating a push subscription
     * @return an instance which can be used by the client
     */
    public static PushSubscription fromString(String str) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException("Cannot create a stream from an empty or null string");
        }
        PushSubscription stream = new PushSubscription();
        stream.id = str;
        return stream;
    }
}
