package com.datasift.client.push.connectors;

import com.datasift.client.push.OutputType;
import com.datasift.client.push.PushConnectors;
import org.cliffc.high_scale_lib.NonBlockingHashSet;

import java.util.Map;
import java.util.Set;

import static com.datasift.client.push.OutputType.BIG_QUERY;
import static com.datasift.client.push.OutputType.COUCH_DB;
import static com.datasift.client.push.OutputType.DYNAMO_DB;
import static com.datasift.client.push.OutputType.ELASTIC_SEARCH;
import static com.datasift.client.push.OutputType.FTP_TYPE;
import static com.datasift.client.push.OutputType.HTTP_TYPE;
import static com.datasift.client.push.OutputType.MONGO_DB;
import static com.datasift.client.push.OutputType.PRECOG;
import static com.datasift.client.push.OutputType.REDIS;
import static com.datasift.client.push.OutputType.S3_OUTPUT;
import static com.datasift.client.push.OutputType.SFTP_OUTPUT;
import static com.datasift.client.push.OutputType.SPLUNK_ENTERPRISE;
import static com.datasift.client.push.OutputType.SPLUNK_STORM;
import static com.datasift.client.push.OutputType.SPLUNK_STORM_REST;
import static com.datasift.client.push.OutputType.ZOOM_DATA;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class BaseConnector<T extends PushConnector<T>> implements PushConnector<T> {
    protected static final String PREFIX = "output_params.";
    protected final Set<String> required;
    protected final Prepared params;
    private T thisRef;
    private OutputType<T> type;

    public BaseConnector(OutputType<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("A valid type is required");
        }
        this.type = type;
        required = new NonBlockingHashSet<String>();
        params = new Prepared(required);
    }

    public static PushConnector fromMap(OutputType<PushConnector> ptype, Map<String, Object> params) {
        String type = ptype.value();
        if (BIG_QUERY.value().equals(type)) {
            return PushConnectors.bigQuery().putAll(params);
        }
        if (COUCH_DB.value().equals(type)) {
            return PushConnectors.couchDB().putAll(params);
        }
        if (DYNAMO_DB.value().equals(type)) {
            return PushConnectors.dynamoDB().putAll(params);
        }
        if (ELASTIC_SEARCH.value().equals(type)) {
            return PushConnectors.elasticSearch().putAll(params);
        }
        if (FTP_TYPE.value().equals(type)) {
            return PushConnectors.ftp().putAll(params);
        }
        if (HTTP_TYPE.value().equals(type)) {
            return PushConnectors.http().putAll(params);
        }
        if (MONGO_DB.value().equals(type)) {
            return PushConnectors.mongoDB().putAll(params);
        }
        if (PRECOG.value().equals(type)) {
            return PushConnectors.precog().putAll(params);
        }
        if (REDIS.value().equals(type)) {
            return PushConnectors.redis().putAll(params);
        }
        if (S3_OUTPUT.value().equals(type)) {
            return PushConnectors.s3().putAll(params);
        }
        if (SFTP_OUTPUT.value().equals(type)) {
            return PushConnectors.sftp().putAll(params);
        }
        if (SPLUNK_STORM_REST.value().equals(type)) {
            return PushConnectors.splunkStormRest().putAll(params);
        }
        if (SPLUNK_ENTERPRISE.value().equals(type)) {
            return PushConnectors.splunkEnterprise().putAll(params);
        }
        if (SPLUNK_STORM.value().equals(type)) {
            return PushConnectors.splunkStorm().putAll(params);
        }
        if (ZOOM_DATA.value().equals(type)) {
            return PushConnectors.zoomdata().putAll(params);
        }
        return null;
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
        params.add(PREFIX + paramName, value);
        return thisRef;
    }

    /**
     * Check if a param has already been set with the given name
     *
     * @param paramName the name to check for
     * @return true if it has, false otherwise
     */
    protected boolean exists(String paramName) {
        return params.has(PREFIX + paramName);
    }

    @Override
    public Prepared parameters() {
        return params;
    }

    @Override
    public OutputType<T> type() {
        return type;
    }

    protected T putAll(Map<String, Object> params) {
        if (params != null) {
            for (Map.Entry<String, Object> e : params.entrySet()) {
                if (e.getValue() != null && e.getKey() != null) {
                    setParam(e.getKey(), String.valueOf(e.getValue()));
                }
            }
        }
        return thisRef;
    }
}
