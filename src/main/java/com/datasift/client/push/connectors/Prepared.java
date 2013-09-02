package com.datasift.client.push.connectors;

import org.cliffc.high_scale_lib.NonBlockingHashMap;

import java.util.Map;
import java.util.Set;

import static java.lang.String.format;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class Prepared {

    protected final Map<String, String> params = new NonBlockingHashMap<String, String>();
    protected final Set<String> required;

    public Prepared(Set<String> required) {
        this.required = required;
    }

    /**
     * Verifies that all required parameters have been set
     *
     * @return a map of the parameters
     */
    public Map<String, String> verifyAndGet() {
        for (String paramName : required) {
            if (params.get(paramName) == null) {
                throw new IllegalStateException(format("Param %s is required but has not been supplied", paramName));
            }
        }
        return params;
    }

    /**
     * Adds a param that'll be sent in the push request
     *
     * @param name  param name
     * @param value param value
     */
    public void add(String name, String value) {
        params.put(name, value);
    }

    /**
     * @param name the name to check
     * @return true if the given name is not null and has a value set
     */
    public boolean has(String name) {
        return name != null && params.get(name) != null;
    }

    /**
     * @return the value associated with the given param name or null if it hasn't been set.
     */
    public String get(String name) {
        return params.get(name);
    }
}
