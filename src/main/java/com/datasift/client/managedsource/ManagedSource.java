package com.datasift.client.managedsource;

import com.datasift.client.BaseDataSiftResult;
import com.datasift.client.managedsource.sources.BaseSource;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.cliffc.high_scale_lib.NonBlockingHashSet;
import org.joda.time.DateTime;

import java.util.Map;
import java.util.Set;

/*
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class ManagedSource extends BaseDataSiftResult {
    @JsonProperty
    protected String name;
    @JsonProperty("source_type")
    protected String sourceType;
    @JsonProperty
    protected Map<String, Object> parameters = new NonBlockingHashMap<String, Object>();
    @JsonProperty
    protected Set<ResourceParams> resources = new NonBlockingHashSet<ResourceParams>();
    @JsonProperty
    protected Set<AuthParams> auth = new NonBlockingHashSet<AuthParams>();
    @JsonProperty("created_at")
    protected long createdAt;
    @JsonProperty
    protected String id;
    @JsonProperty
    protected String status;

    public static ManagedSource fromString(String str) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException("Cannot create a managed source from an empty or null string");
        }
        ManagedSource stream = new ManagedSource();
        stream.id = str;
        return stream;
    }

    public String getName() {
        return name;
    }

    public String getSourceType() {
        return sourceType;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public Set<ResourceParams> getResources() {
        return resources;
    }

    public Set<AuthParams> getAuth() {
        return auth;
    }

    public DateTime getCreatedAt() {
        return new DateTime(createdAt);
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public static class AuthParams extends BaseSource.AuthParams {
        @JsonProperty("identity_id")
        protected String identityId;
        @JsonProperty("source_id")
        protected String sourceId;
        @JsonProperty
        protected String status;

        protected AuthParams() {
        }

        public String identityId() {
            return identityId;
        }

        public String sourceId() {
            return sourceId;
        }

        public String status() {
            return status;
        }
    }

    public static class ResourceParams extends BaseSource.ResourceParams {
        @JsonProperty("identity_id")
        protected String identityId;
        @JsonProperty("source_id")
        protected String sourceId;
        @JsonProperty
        protected String status;

        protected ResourceParams() {
        }

        public String identityId() {
            return identityId;
        }

        public String sourceId() {
            return sourceId;
        }

        public String status() {
            return status;
        }
    }
}
