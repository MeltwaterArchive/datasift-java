package com.datasift.client.accounts;

import com.datasift.client.BaseDataSiftResult;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NewLimit extends BaseDataSiftResult {
    @JsonProperty("service")
    protected String service;
    @JsonProperty("total_allowance")
    protected Long totalAllowance;

    public NewLimit(String s, Long t) {
        service = s;
        totalAllowance = t;
    }
}
