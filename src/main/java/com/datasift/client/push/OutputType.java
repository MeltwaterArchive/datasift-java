package com.datasift.client.push;

import com.datasift.client.push.connectors.BigQuery;
import com.datasift.client.push.connectors.CouchDB;
import com.datasift.client.push.connectors.DynamoDB;
import com.datasift.client.push.connectors.ElasticSearch;
import com.datasift.client.push.connectors.FTP;
import com.datasift.client.push.connectors.Http;
import com.datasift.client.push.connectors.MongoDB;
import com.datasift.client.push.connectors.Precog;
import com.datasift.client.push.connectors.PushConnector;
import com.datasift.client.push.connectors.Redis;
import com.datasift.client.push.connectors.S3;
import com.datasift.client.push.connectors.SFTP;
import com.datasift.client.push.connectors.SplunkEnterprise;
import com.datasift.client.push.connectors.SplunkStorm;
import com.datasift.client.push.connectors.SplunkStormRest;
import com.datasift.client.push.connectors.ZoomData;

/**
 * Represents one of the possible push destinations
 *
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class OutputType<T extends PushConnector> {
    public static final OutputType<BigQuery> BIG_QUERY = new OutputType<BigQuery>("bigquery");
    public static final OutputType<CouchDB> COUCH_DB = new OutputType<CouchDB>("couchdb");
    public static final OutputType<DynamoDB> DYNAMO_DB = new OutputType<DynamoDB>("dynamodb");
    public static final OutputType<ElasticSearch> ELASTIC_SEARCH = new OutputType<ElasticSearch>("elasticsearch");
    public static final OutputType<FTP> FTP_TYPE = new OutputType<FTP>("ftp");
    public static final OutputType<Http> HTTP_TYPE = new OutputType<Http>("http");
    public static final OutputType<MongoDB> MONGO_DB = new OutputType<MongoDB>("mongodb");
    public static final OutputType<Precog> PRECOG = new OutputType<Precog>("precog");
    public static final OutputType<Redis> REDIS = new OutputType<Redis>("redis");
    public static final OutputType<S3> S3_OUTPUT = new OutputType<S3>("s3");
    public static final OutputType<SFTP> SFTP_OUTPUT = new OutputType<SFTP>("sftp");
    public static final OutputType<SplunkStormRest> SPLUNK_STORM_REST =
            new OutputType<SplunkStormRest>("splunkstormrest");
    public static final OutputType<SplunkStorm> SPLUNK_STORM = new OutputType<SplunkStorm>("splunkstorm");
    public static final OutputType<SplunkEnterprise> SPLUNK_ENTERPRISE = new OutputType<SplunkEnterprise>("splunk");
    public static final OutputType<ZoomData> ZOOM_DATA = new OutputType<ZoomData>("zoomdata");
    private final String value;

    public OutputType(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Output type value cannot be null");
        }
        this.value = value;
    }

    public static <T extends PushConnector> OutputType<T> fromString(String type) {
        return new OutputType<T>(type);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OutputType that = (OutputType) o;
        return !(value != null ? !value.equals(that.value) : that.value != null);
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
