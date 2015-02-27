package com.datasift.client.analysis;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class AnalyzeResultEntryList {
    @JsonProperty
    protected final List<AnalyzeResultEntry> data = new ArrayList<AnalyzeResultEntry>();
    private final int count;

    public AnalyzeResultEntryList() {
        count = 0;
    }

    @JsonCreator
    public AnalyzeResultEntryList(List<AnalyzeResultEntry> data) {
        if (data != null) {
            this.data.addAll(data);
        }
        this.count = data.size();
    }

    public List<AnalyzeResultEntry> getData() {
        return this.data;
    }

    public int getCount() {
        return this.count;
    }
}
