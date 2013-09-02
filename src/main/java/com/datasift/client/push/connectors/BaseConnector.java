package com.datasift.client.push.connectors;

import org.cliffc.high_scale_lib.NonBlockingHashSet;

import java.util.Set;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class BaseConnector<T> implements PushConnector {
    protected static final String PREFIX = "output_params.";
    protected final Set<String> required;
    protected final Prepared params;
    private T thisRef;

    public BaseConnector() {
        required = new NonBlockingHashSet<String>();
        params = new Prepared(required);
    }

    /**
     * To make it convenient for all sub-types to just return the results of setParam
     * the sub type should call setup passing it's this reference
     *
     * @param ref "this" in any sub type
     */
    protected void setup(T ref, String... requiredParams) {
        thisRef = ref;
        if (requiredParams == null) {
            throw new IllegalArgumentException("Set of required params cannot be null");
        }
        for (String param : requiredParams) {
            required.add(PREFIX + param);
        }
    }

    protected T setParam(String paramName, String value) {
        if (paramName == null || value == null) {
            throw new IllegalArgumentException(paramName + " is null but no parameters are allowed to be");
        }
        params.add(paramName, value);
        return thisRef;
    }

    @Override
    public Prepared parameters() {
        return params;
    }
}
