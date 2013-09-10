package com.datasift.client.managedsource.sources;

import com.datasift.client.DataSiftClient;
import com.datasift.client.DataSiftConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class BaseSource<T extends DataSource> implements DataSource {
    public static final Param PARAMETERS = new Param("parameters"), RESOURCES = new Param("resources"),
            AUTH = new Param("auth");
    protected DataSiftConfig config;
    protected Map<String, Map<String, Object>> params = new NonBlockingHashMap<String, Map<String, Object>>();
    protected T thisParam;
    protected Logger log = LoggerFactory.getLogger(getClass());

    public BaseSource() {
        params.put(PARAMETERS.value(), new NonBlockingHashMap<String, Object>());
        params.put(RESOURCES.value(), new NonBlockingHashMap<String, Object>());
        params.put(AUTH.value(), new NonBlockingHashMap<String, Object>());
    }

    public void setup(T thisParam, DataSiftConfig config) {
        this.thisParam = thisParam;
        this.config = config;
    }

    protected T setParam(String name, Object value, Param type) {
        if (name == null || name.isEmpty() || value == null) {
            throw new IllegalArgumentException("Both name and value are required");
        }
        if (params.get(type.value()) == null) {
            params.put(type.value(), new NonBlockingHashMap<String, Object>());
        }
        params.get(type.value()).put(name, value);
        return thisParam;
    }

    /**
     * @return a URL encoded set of parameters or null if an error occurred
     */
    @Override
    public String getURLEncoded() {
        try {
            return URLEncoder.encode(DataSiftClient.MAPPER.writeValueAsString(params()), config.urlEncodingFormat());
        } catch (UnsupportedEncodingException e) {
            log.warn("Failed to encode parameters", e);
        } catch (JsonProcessingException e) {
            log.warn("Failed to encode parameters", e);
        }
        return null;
    }

    @Override
    public Map<String, Map<String, Object>> params() {
        return params;
    }

    public static class Param {
        private String value;

        public Param(String value) {
            if (value == null || value.isEmpty()) {
                throw new IllegalArgumentException("Value can't be null or empty");
            }
            this.value = value;
        }

        public String value() {
            return value;
        }
    }
}
