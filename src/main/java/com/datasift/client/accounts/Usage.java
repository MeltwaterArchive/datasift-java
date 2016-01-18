package com.datasift.client.accounts;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Usage {
    @JsonProperty("quantity")
    protected double quantity;
    @JsonProperty("cost")
    protected int cost;
    @JsonProperty("category")
    protected String category;
    @JsonProperty("unit")
    protected String unit;
    @JsonProperty("source")
    protected String source;
    @JsonProperty("timestamp")
    protected long timestamp;

    public double quantity() {
        return quantity;
    }

    public int cost() {
        return cost;
    }

    public String category() {
        return category;
    }

    public String unit() {
        return unit;
    }

    public String source() {
        return source;
    }

    public long timestamp() {
        return timestamp;
    }
}
