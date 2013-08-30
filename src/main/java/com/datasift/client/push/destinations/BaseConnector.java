package com.datasift.client.push.destinations;

import org.cliffc.high_scale_lib.NonBlockingHashMap;

import java.util.Map;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class BaseConnector<T> implements PushConnector {
    protected static final String PREFIX = "output_params.";
    protected Map<String, String> params = new NonBlockingHashMap<String, String>();
    private T thisRef;

    /**
     * To make it convenient for all sub-types to just return the results of setParam
     * the sub type should call setup passing it's this reference
     *
     * @param ref "this" in any sub type
     */
    protected void setup(T ref) {
        thisRef = ref;
    }

    /**
     * Adds a parameter that will be used to configure this connector
     * This will automatically prefic the parameter name with "output_params."
     * so that a name of "bucket" becomes "output_params.bucket"
     *
     * @param name  the parameter name
     * @param value the parameter value
     * @return this
     */
    protected BaseConnector addParam(String name, String value) {
        return addParam(name, value, true);
    }

    /**
     * Adds a parameter that will be used to configure this connector
     *
     * @param name      the parameter name
     * @param value     the parameter value
     * @param addPrefix if true then the prefix "output_params."   is added to the name of the parameter.
     *                  This is the default behaviour so that a param name of
     *                  "bucket" will become "output_params.bucket"
     * @return this
     */
    protected BaseConnector addParam(String name, String value, boolean addPrefix) {
        if (name == null || value == null) {
            throw new IllegalArgumentException("Both name and value are required");
        }
        params.put((addPrefix ? PREFIX : "") + name, value);
        return this;
    }

    protected T setParam(String paramName, String value, boolean required) {
        if (value == null) {
            throw new IllegalArgumentException(paramName + " is null but no parameters are allowed to be");
        }
        if (required && value.isEmpty()) {
            throw new IllegalArgumentException("An " + paramName + " must be provided");
        }
        addParam(paramName, value);
        return thisRef;
    }

    @Override
    public Map<String, String> parameters() {
        return params;
    }
}
