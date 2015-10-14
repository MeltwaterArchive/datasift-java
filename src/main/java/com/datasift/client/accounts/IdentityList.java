package com.datasift.client.accounts;

import com.datasift.client.APIDataSiftResult;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class IdentityList extends APIDataSiftResult implements Iterable<Identity> {
    @JsonProperty
    private final List<Identity> identities = new ArrayList<Identity>();
    @JsonProperty
    private int count;
    @JsonProperty
    private int page;
    @JsonProperty
    private int pages;
    @JsonProperty("per_page")
    private int perPage;

    /**
     * @return all the available identities. May be empty but will never be null
     */
    public List<Identity> getIdentities() {
        return identities;
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
    public Iterator<Identity> iterator() {
        return identities.iterator();
    }
}
