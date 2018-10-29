package com.datasift.client.accounts;

import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * Created by andi on 24/04/15.
 */
public class NewTokenValue {
    @JsonProperty("token")
    protected String token;
    public NewTokenValue(String t) {
        token = t;
    }
}
