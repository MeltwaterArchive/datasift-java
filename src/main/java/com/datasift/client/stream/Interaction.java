package com.datasift.client.stream;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author Courtney Robinson <courtney@crlog.info>
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

    @Override
    public String toString() {
        return data.toString();
    }
}
