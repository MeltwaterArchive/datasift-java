package com.datasift.client;

public class RateLimit {
    private int limit;
    private int remaining;
    private int cost;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getRemaining() {
        return remaining;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "RateLimit{" +
                "limit=" + limit +
                ", remaining=" + remaining +
                ", cost=" + cost +
                '}';
    }
}
