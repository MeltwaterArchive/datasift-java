package com.datasift.client.dynamiclist.replace;

import com.datasift.client.BaseDataSiftResult;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Christopher Gilbert <christopher.john.gilbert@gmail.com>
 */
public class ReplaceList extends BaseDataSiftResult {
    @JsonProperty
    protected String id;

    public String getId() { return id; }

    public static ReplaceList fromString(String str) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException("Cannot create a dynamic replace list from an empty or null string");
        }
        ReplaceList list = new ReplaceList();
        list.id = str;
        return list;
    }
}
