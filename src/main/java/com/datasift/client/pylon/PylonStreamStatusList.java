package com.datasift.client.pylon;

import com.datasift.client.BaseDataSiftResult;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class PylonStreamStatusList extends BaseDataSiftResult {
    @JsonProperty
    protected final List<PylonStreamStatus> data = new ArrayList<PylonStreamStatus>();
    private final int count;

    public PylonStreamStatusList() {
        count = 0;
    }

    @JsonCreator
    public PylonStreamStatusList(List<PylonStreamStatus> data) {
        if (data != null) {
            this.data.addAll(data);
        }
        this.count = data.size();
    }

    public List<PylonStreamStatus> getData() {
        return this.data;
    }

    public int getCount() {
        return this.count;
    }
}
