package com.datasift.client.accounts;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NewLimitValue {
    @JsonProperty("total_allowance")
    protected Long total_allowance;
    public NewLimitValue(Long t) {
        total_allowance = t;
    }
}
