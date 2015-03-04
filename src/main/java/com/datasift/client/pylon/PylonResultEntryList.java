package com.datasift.client.pylon;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class PylonResultEntryList {
    @JsonProperty
    protected final List<PylonResultEntry> data = new ArrayList<PylonResultEntry>();
    private final int count;

    public PylonResultEntryList() {
        count = 0;
    }

    @JsonCreator
    public PylonResultEntryList(List<PylonResultEntry> data) {
        if (data != null) {
            this.data.addAll(data);
        }
        this.count = data.size();
    }

    public List<PylonResultEntry> getData() {
        return this.data;
    }

    public int getCount() {
        return this.count;
    }
}
