package com.datasift.client.push;

import com.datasift.client.push.connectors.BigQuery;
import com.datasift.client.push.connectors.CouchDB;
import com.datasift.client.push.connectors.DynamoDB;
import com.datasift.client.push.connectors.ElasticSearch;
import com.datasift.client.push.connectors.S3;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class PushConnectors {
    private PushConnectors() {
    }

    /**
     * See the <a href="http://dev.datasift.com/docs/push/connectors/bigquery">official documentation</a>
     * <p/>
     * Data format delivered:
     * <p/>
     * Google BigQuery native format. Each interaction is stored as one table row.
     * <p/>
     * Storage type:
     * For each delivery, DataSift sends one file containing all the available interactions.
     * <p/>
     * Limitations:
     * Google BigQuery import limits and your Google BigQuery quotas.
     * <p/>
     * Links:
     * There are many Google BigQuery SDKs available. Please refer to the Google BigQuery SDK list.
     *
     * @return a new configurable BigQuery connector
     */
    public static BigQuery bigQuery() {
        return new BigQuery();
    }

    /**
     * See the <a href="http://dev.datasift.com/docs/push/connectors/s3">official documentation</a>
     * <p/>
     * Data format delivered:
     * <p/>
     * JSON document.
     * <p/>
     * Storage type:
     * For each delivery, DataSift sends one file containing all the available interactions.
     * <p/>
     * Limitations:
     * Take care when you set the max_size and delivery_frequency output parameters. If your stream generates data at
     * a faster rate than you permit the delivery, the buffer will fill up until we reach the point where data may be
     * discarded.
     * <p/>
     * Links:
     * There are a large number of S3 SDKs available. Please refer to the Amazon AWS SDK list.
     *
     * @return a new configurable S3 connector
     */
    public static S3 s3() {
        return new S3();
    }

    /**
     * Data format delivered:
     * CouchDB native format (JSON). Each interaction is stored as one document.
     * Storage type:
     * One document per interaction.
     * Limitations:
     * There is no set limit to the number of values or amount of data that columns can hold.
     * Links:
     * Take a look at the <a href="http://www.couchbase.org/content/couchdb-drivers">CouchDB Drivers.</a>
     */
    public static CouchDB couchDB() {
        return new CouchDB();
    }

    public static DynamoDB dynamoDB() {
        return new DynamoDB();
    }

    public static ElasticSearch elasticSearch() {
        return new ElasticSearch();
    }
}
