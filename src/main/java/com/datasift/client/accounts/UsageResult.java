package com.datasift.client.accounts;

import com.datasift.client.BaseDataSiftResult;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UsageResult extends BaseDataSiftResult implements Iterable<Usage> {
    @JsonProperty("usage")
    private final List<Usage> usageList = new ArrayList<>();

    /**
     * @return all the available usage periods. May be empty but will never be null
     */
    public List<Usage> getUsageList() { return usageList; }

    @Override
    public Iterator<Usage> iterator() {
        return usageList.iterator();
    }
}
