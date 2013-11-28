package com.datasift.client.stream;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class Interaction {
    private final JsonNode data;

    public Interaction(JsonNode data) {
        if (data == null) {
            throw new IllegalArgumentException("Can't create an interaction from a null node");
        }
        this.data = data;
    }

    public JsonNode getData() {
        return data;
    }

    /**
     * Fetches values using the popular dot notation
     * i.e. twitter.user.id would return the id (12345) value in a structure similar to
     * <pre>
     * {"twitter" : {
     *      "user" : {
     *          "id" : 12345
     *      }
     *   }
     * }
     * </pre>
     *
     * @param str a JSON dot notation string
     * @return null if a value doesn't exist for tht key or the value
     */
    public JsonNode get(String str) {
        String[] parts = str.split("\\.");
        JsonNode retval = data.get(parts[0]);
        for (int i = 1; i < parts.length - 1; i++) {
            if (retval == null) {
                return null;
            } else {
                retval = retval.get(parts[i]);
            }
        }
        return retval;
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
