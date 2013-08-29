package com.datasift.client.stream;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class DeletedInteraction {
    protected JsonNode data;

    public DeletedInteraction(MultiStreamInteraction mi) {
        data = mi.getData();
    }

    /**
     * @return the ID provided by DataSift for the original interaction
     */
    public String interactionId() {
        return data.get("interaction").get("id").asText();
    }

    /**
     * @return the interaction type, currently only "twitter"
     */
    public String interactionType() {
        return data.get("interaction").get("type").asText();
    }

    /**
     * @return the Tweet ID
     */
    public String tweetId() {
        return data.get("twitter").get("id").asText();
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
