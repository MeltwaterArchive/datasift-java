package com.datasift.client.dynamiclist;

import com.datasift.client.BaseDataSiftResult;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Christopher Gilbert <christopher.john.gilbert@gmail.com>
 */
public class DynamicList extends BaseDataSiftResult {
    @JsonProperty
    protected String id;

    public String getId() { return id; }

    public static DynamicList fromString(String str) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException("Cannot create a dynamic list from an empty or null string");
        }
        DynamicList list = new DynamicList();
        list.id = str;
        return list;
    }
}
