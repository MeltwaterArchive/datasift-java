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
    protected DataSiftConfig config;
    protected Map<String, String> params = new NonBlockingHashMap<String, String>();
    protected T thisParam;
    protected Logger log = LoggerFactory.getLogger(getClass());

    public void setup(T thisParam, DataSiftConfig config) {
        this.thisParam = thisParam;
        this.config = config;
    }

    protected T setParam(String name, String value) {
        if (name == null || name.isEmpty() || value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Both name and value are required");
        }

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
    public Map<String, String> params() {
        return params;
    }
}
