package com.datasift.client.pylon;

import com.datasift.client.BaseDataSiftResult;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class PylonRecordingList extends BaseDataSiftResult {
    @JsonProperty
    protected List<PylonRecording> data = new ArrayList<PylonRecording>();
    @JsonProperty
    protected int count;
    @JsonProperty
    protected int page;
    @JsonProperty("per_page")
    protected int perPage;
    @JsonProperty
    protected int pages;

    public PylonRecordingList() {
    }

    public int getCount() {
        return this.count;
    }

    public List<PylonRecording> getData() {
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
