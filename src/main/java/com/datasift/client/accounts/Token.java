package com.datasift.client.accounts;

import com.datasift.client.BaseDataSiftResult;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/*
 * Created by andi on 24/04/15.
 */
public class Token extends BaseDataSiftResult {

    @JsonProperty("service")
    protected String service;
    @JsonProperty("token")
    protected String token;

    @JsonProperty("created_at")
    protected long createdAt;
    @JsonProperty("updated_at")
    protected long updatedAt;
    @JsonProperty("expires_at")
    protected long expiresAt;

    public String service() {
        return service;
    }

    public String token() {
        return token;
    }

    public long createdAt() {
        return createdAt;
    }

    public long updatedAt() {
        return updatedAt;
    }

    public long expiresAt() {
        return expiresAt;
    }

    public Date createdAtAsDate() {
        return new Date(TimeUnit.SECONDS.toMillis(createdAt));
    }

    public Date updatedAtAsDate() {
        return new Date(TimeUnit.SECONDS.toMillis(updatedAt));
    }

    public Date expiresAtAsDate() {
        return new Date(TimeUnit.SECONDS.toMillis(expiresAt));
    }

}
