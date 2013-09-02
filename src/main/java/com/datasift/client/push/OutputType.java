package com.datasift.client.push;

import com.datasift.client.push.connectors.BigQuery;
import com.datasift.client.push.connectors.CouchDB;
import com.datasift.client.push.connectors.DynamoDB;
import com.datasift.client.push.connectors.ElasticSearch;
import com.datasift.client.push.connectors.FTP;
import com.datasift.client.push.connectors.Http;
import com.datasift.client.push.connectors.MongoDB;
import com.datasift.client.push.connectors.Precog;
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
public class OutputType<T> {
    public static final OutputType<BigQuery> BIG_QUERY = new OutputType("bigquery");
    public static final OutputType<CouchDB> COUCH_DB = new OutputType("couchdb");
    public static final OutputType<DynamoDB> DYNAMO_DB = new OutputType("dynamodb");
    public static final OutputType<ElasticSearch> ELASTIC_SEARCH = new OutputType("elasticsearch");
    public static final OutputType<FTP> FTP_TYPE = new OutputType("ftp");
    public static final OutputType<Http> HTTP_TYPE = new OutputType("http");
    public static final OutputType<MongoDB> MONGO_DB = new OutputType("mongodb");
    public static final OutputType<Precog> PRECOG = new OutputType("precog");
    public static final OutputType<Redis> REDIS = new OutputType("redis");
    public static final OutputType<S3> S3_OUTPUT = new OutputType("s3");
    public static final OutputType<SFTP> SFTP_OUTPUT = new OutputType("sftp");
    public static final OutputType<SplunkStormRest> SLUNK_STORM_REST = new OutputType("splunkstormrest");
    public static final OutputType<SplunkStorm> SPLUNK_STORM = new OutputType("splunkstorm");
    public static final OutputType<SplunkEnterprise> SPLUNK_ENTERPRISE = new OutputType("splunk");
    public static final OutputType<ZoomData> ZOOM_DATA = new OutputType("zoomdata");
    private final String value;

    public OutputType(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Output type value cannot be null");
        }
        this.value = value;
    }

    public String value() {
        return value;
    }
}
