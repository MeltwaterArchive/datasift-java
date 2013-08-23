package com.datasift.client;

import io.netty.handler.codec.http.QueryStringEncoder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class ParamBuilder {
    protected Map<String, Object> params = new HashMap<>();

    /**
     * Adds or replaces a parameter
     *
     * @param name  the name of the parameter
     * @param value the value to the parameter
     * @return this builder for further re-use
     */
    public ParamBuilder put(String name, Object value) {
        params.put(name, value);
        return this;
    }

    /**
     * @param uri A URL to which the query string will be appended
     * @return a URI with the query string configured with params of this object
     */
    public URI forURL(URI uri) {
        QueryStringEncoder encoder = new QueryStringEncoder(uri.toString());
        for (Map.Entry<String, Object> e : params.entrySet()) {
            encoder.addParam(e.getKey(), String.valueOf(e.getValue()));
        }
        try {
            return encoder.toUri();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Cannot construct a query string from the given params. " +
                    "An invalid parameter was used.");
        }
    }

    /**
     * @return The underlying map used to represent all configured params. This can be modified externally.
     */
    public Map<String, Object> getParams() {
        return params;
    }
}
