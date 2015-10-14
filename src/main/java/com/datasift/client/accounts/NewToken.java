package com.datasift.client.accounts;

import com.datasift.client.APIDataSiftResult;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by andi on 24/04/15.
 */
public class NewToken extends APIDataSiftResult {
    @JsonProperty("service")
    protected String service;
    @JsonProperty("token")
    protected String token;

    public NewToken(String s, String t) {
        service = s;
        token = t;
    }
}
