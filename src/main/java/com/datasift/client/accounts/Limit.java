package com.datasift.client.accounts;

import com.datasift.client.BaseDataSiftResult;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by andi on 24/04/15.
 */
public class Limit extends BaseDataSiftResult {

    @JsonProperty("identity_id")
    protected String identityId;
    @JsonProperty("service")
    protected String service;

    @JsonProperty("total_allowance")
    protected long totalAllowance;

    public String identityId() {
        return identityId;
    }

    public String service() {
        return service;
    }

    public long totalAllowance() {
        return totalAllowance;
    }


}
