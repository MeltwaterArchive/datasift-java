package com.datasift.client.push;

import com.datasift.client.DataSiftResult;
import com.datasift.client.stream.Interaction;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class PulledInteractions extends DataSiftResult {
    @JsonProperty
    private List<Interaction> interactions = new ArrayList<Interaction>();

    public void data(List<Interaction> interactions) {
        if (interactions != null) {
            this.interactions.addAll(interactions);
        }
    }

    /**
     * @return A list of interactions that were pulled in this result, will never return null but  the list may be empty
     */
    public List<Interaction> getInteractions() {
        return interactions;
    }
}
