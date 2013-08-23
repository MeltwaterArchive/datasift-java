package com.datasift.client;

import java.util.HashSet;
import java.util.Set;

/**
 * Marks a response failed for one or more reasons
 *
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class FailedResponse {
    private Set<Throwable> reasons = new HashSet<>();

    public FailedResponse(Throwable e) {
        reasons.add(e);
    }

    /**
     * @return A set of exceptions which caused the request to fail
     */
    public Set<Throwable> reasons() {
        return reasons;
    }
}
