package com.datasift.client.push.destinations;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class OutputTypeBuilder {
    private OutputTypeBuilder() {
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
    public static BigQueryConnector bigQuery() {
        return new BigQueryConnector();
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
    public static S3Connector s3() {
        return new S3Connector();
    }
}
