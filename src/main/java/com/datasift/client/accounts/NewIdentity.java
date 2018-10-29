package com.datasift.client.accounts;

import com.datasift.client.BaseDataSiftResult;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * Created by andi on 24/04/15.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewIdentity extends BaseDataSiftResult {
    @JsonProperty("label")
    String label;
    @JsonProperty("status")
    String status;
    @JsonProperty("master")
    Boolean master;

    public NewIdentity(String l, String s, Boolean m) {
        label = l;
        status = s;
        master = m;
    }
}
