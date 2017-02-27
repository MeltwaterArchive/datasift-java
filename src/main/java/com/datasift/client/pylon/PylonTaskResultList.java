package com.datasift.client.pylon;

import com.datasift.client.BaseDataSiftResult;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class PylonTaskResultList extends BaseDataSiftResult {
    @JsonProperty
    protected List<PylonTaskResult> tasks = new ArrayList<PylonTaskResult>();
    @JsonProperty
    protected int count;
    @JsonProperty
    protected int page;
    @JsonProperty("per_page")
    protected int perPage;
    @JsonProperty
    protected int pages;

    public PylonTaskResultList() {
    }

    public int getCount() {
        return this.count;
    }

    public List<PylonTaskResult> getTasks() {
        return tasks;
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
