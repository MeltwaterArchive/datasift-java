package com.datasift.client.pylon;

import com.datasift.client.BaseDataSiftResult;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PylonSample extends BaseDataSiftResult {
    @JsonProperty("remaining")
    protected int rateLimitRemaining;
    @JsonProperty("reset_at")
    protected int rateLimitResetTime;
    @JsonProperty
    protected List<PylonSampleInteractionItem> interactions = new ArrayList<>();

    public PylonSample() { }

    public int getRemainingRateLimit() { return this.rateLimitRemaining; }

    public int getRateLimitResetTime() { return this.rateLimitResetTime; }

    /**
     * @return All available interactions. May be empty but will never be null
     */
    public List<PylonSampleInteractionItem> getInteractions() {
        return interactions;
    }

    public Iterator<PylonSampleInteractionItem> iterator() {
        return interactions.iterator();
    }
}
