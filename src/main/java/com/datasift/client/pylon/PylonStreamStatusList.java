package com.datasift.client.pylon;

import com.datasift.client.DataSiftAPIResult;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class PylonStreamStatusList extends DataSiftAPIResult {
    @JsonProperty
    protected List<PylonStreamStatus> data = new ArrayList<PylonStreamStatus>();
    @JsonProperty
    protected int count;
    @JsonProperty
    protected int page;
    @JsonProperty("per_page")
    protected int perPage;
    @JsonProperty
    protected int pages;

    public PylonStreamStatusList() {
    }

    public int getCount() {
        return this.count;
    }

    public List<PylonStreamStatus> getData() {
        return data;
    }

    public int getPage() {
        return page;
    }

    public int getPerPage() {
        return perPage;
    }

    public int getPages() {
        return pages;
    }
}
