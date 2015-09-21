package com.datasift.client.accounts;

import com.datasift.client.BaseDataSiftResult;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class TokenList extends BaseDataSiftResult implements Iterable<Token> {
    @JsonProperty
    private final List<Token> tokens = new ArrayList<Token>();
    @JsonProperty
    private int count;
    @JsonProperty
    private int page;
    @JsonProperty
    private int pages;
    @JsonProperty("per_page")
    private int perPage;

    /**
     * @return all the available tokens. May be empty but will never be null
     */
    public List<Token> getIdentities() {
        return tokens;
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
    public Iterator<Token> iterator() {
        return tokens.iterator();
    }
}
