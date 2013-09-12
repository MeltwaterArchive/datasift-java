package com.datasift.client.managedsource.sources;

import com.datasift.client.DataSiftClient;
import com.datasift.client.DataSiftConfig;
import com.datasift.client.managedsource.ManagedDataSourceType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.cliffc.high_scale_lib.NonBlockingHashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public abstract class BaseSource<T extends DataSource> implements DataSource<T> {
    protected final ManagedDataSourceType<T> type;
    protected DataSiftConfig config;
    protected Logger log = LoggerFactory.getLogger(getClass());
    protected Map<String, Object> parameters = new NonBlockingHashMap<String, Object>();
    protected Set<ResourceParams> resources = new NonBlockingHashSet<ResourceParams>();
    protected Set<AuthParams> auth = new NonBlockingHashSet<AuthParams>();

    public BaseSource(DataSiftConfig config, ManagedDataSourceType<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("A type is required, cannot be null");
        }
        this.config = config;
        this.type = type;
    }

    public ManagedDataSourceType<T> type() {
        return type;
    }

    protected T setParametersField(String name, Object value) {
        if (name == null || name.isEmpty() || value == null) {
            throw new IllegalArgumentException("Both name and value are required");
        }
        parameters.put(name, value);
        return (T) this;
    }

    /**
     * Creates a set of auth parameters that can be used for this source
     *
     * @param name    a human friendly name for this auth set
     * @param expires identity resource expiry date/time as a UTC timestamp
     * @return a new auth set
     */
    public AuthParams newAuthParams(String name, long expires) {
        AuthParams set = new AuthParams();
        set.name(name);
        set.expires(expires);
        auth.add(set);
        return set;
    }

    /**
     * @return A new set of resource parameters
     */
    public ResourceParams newResourceParams() {
        ResourceParams set = new ResourceParams();
        resources.add(set);
        return set;
    }

    public String getURLEncodedParameters() {
        try {
            return URLEncoder.encode(DataSiftClient.MAPPER.writeValueAsString(parameters), config.urlEncodingFormat());
        } catch (UnsupportedEncodingException e) {
            log.warn("Failed to encode parameters", e);
        } catch (JsonProcessingException e) {
            log.warn("Failed to encode parameters", e);
        }
        return null;
    }

    public String getURLEncodedResources() {
        try {
            return URLEncoder.encode(DataSiftClient.MAPPER.writeValueAsString(parameters), config.urlEncodingFormat());
        } catch (UnsupportedEncodingException e) {
            log.warn("Failed to encode parameters", e);
        } catch (JsonProcessingException e) {
            log.warn("Failed to encode parameters", e);
        }
        return null;
    }

    public String getURLEncodedAuth() {
        try {
            return URLEncoder.encode(DataSiftClient.MAPPER.writeValueAsString(parameters), config.urlEncodingFormat());
        } catch (UnsupportedEncodingException e) {
            log.warn("Failed to encode parameters", e);
        } catch (JsonProcessingException e) {
            log.warn("Failed to encode parameters", e);
        }
        return null;
    }

    public static class ResourceParams {
        @JsonProperty
        private Map<String, Object> parameters = new NonBlockingHashMap<String, Object>();
        @JsonProperty
        private String name;

        private ResourceParams() {
        }

        public Map<String, Object> getParameters() {
            return parameters;
        }

        public void set(String name, Object value) {
            parameters.put(name, value);
        }

        public void name(String name) {
            this.name = name;
        }
    }

    public static class AuthParams {
        @JsonProperty
        private Map<String, Object> parameters = new NonBlockingHashMap<String, Object>();
        @JsonProperty("expires_at")
        private long expires;
        @JsonProperty
        private String name;

        private AuthParams() {
        }

        public Map<String, Object> getParameters() {
            return parameters;
        }

        public void set(String name, String value) {
            parameters.put(name, value);
        }

        public void expires(long expires) {
            this.expires = expires;
        }

        public void name(String name) {
            this.name = name;
        }
    }
}
