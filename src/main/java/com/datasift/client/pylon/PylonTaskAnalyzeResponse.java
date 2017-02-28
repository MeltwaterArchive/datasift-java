package com.datasift.client.pylon;

import com.datasift.client.BaseDataSiftResult;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PylonTaskAnalyzeResponse extends BaseDataSiftResult {

    public PylonTaskAnalyzeResponse() {
    }

    @JsonProperty
    protected String id;

    public String getId() {
        return id;
    }

}
