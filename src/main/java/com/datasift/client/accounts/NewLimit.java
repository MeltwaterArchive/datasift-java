package com.datasift.client.accounts;

import com.datasift.client.DataSiftAPIResult;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NewLimit extends DataSiftAPIResult {
    @JsonProperty("service")
    protected String service;
    @JsonProperty("total_allowance")
    protected Long totalAllowance;

    public NewLimit(String s, Long t) {
        service = s;
        totalAllowance = t;
    }
}
