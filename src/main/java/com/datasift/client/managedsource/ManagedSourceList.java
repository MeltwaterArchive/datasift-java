package com.datasift.client.managedsource;

import com.datasift.client.APIDataSiftResult;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class ManagedSourceList extends APIDataSiftResult implements Iterable<ManagedSource> {
    @JsonProperty
    private final List<ManagedSource> sources = new ArrayList<ManagedSource>();
    @JsonProperty
    private int count;
    @JsonProperty
    private int page;
    @JsonProperty
    private int pages;
    @JsonProperty("per_page")
    private int perPage;

    /**
     * @return all the available sources. May be empty but will never be null
     */
    public List<ManagedSource> getSources() {
        return sources;
    }

    public int getCount() {
        return count;
    }

    public int getPage() {
        return page;
    }

    public int getPages() {
        return pages;
    }

    public int getPerPage() {
        return perPage;
    }

    @Override
    public Iterator<ManagedSource> iterator() {
        return sources.iterator();
    }
}
