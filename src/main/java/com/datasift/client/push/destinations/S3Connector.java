package com.datasift.client.push.destinations;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class S3Connector extends BaseConnector<S3Connector> {
    protected S3Connector() {
        setup(this);
    }

    /**
     * Sets the output format for your data
     *
     * @param format one of the allowed S3 formats, defaults to json_meta
     * @return this
     */
    public S3Connector format(S3OutputFormat format) {
        String strFormat;
        switch (format) {
            case JSON_ARRAY:
                strFormat = "json_array";
                break;
            case JSON_NEW_LINE:
                strFormat = "json_new_line";
                break;
            case JSON_META:
            default:
                strFormat = "json_meta";
                break;
        }
        addParam("format", strFormat);
        return this;
    }

    /**
     * @param key The access key for the S3 account that DataSift will send to. Make sure that this value is properly
     *            encoded, otherwise your /push/create request will fail.
     * @return this
     */
    public S3Connector accessKey(String key) {
        return setParam("access_key", key, true);
    }

    /**
     * @param secret The secret key for the S3 account that DataSift will send to.     Make sure that this value is
     *               properly encoded, otherwise your /push/create request will fail.
     * @return this
     */
    public S3Connector secretKey(String secret) {
        return setParam("secret_key", secret, true);
    }

    /**
     * The minimum number of seconds you want DataSift to wait before sending data again:
     * 10 (10 seconds)
     * 30 (30 seconds)
     * 60 (1 minute)
     * 300 (5 minutes)
     * 900 (15 minutes)
     * In reality, a stream might not have data available after the wait. Typically this happens in streams that have
     * very tight filtering constraints, so the wait time might be longer than you specify. If your system is capable
     * of handling large amounts of incoming data, you can turn on continuous delivery:
     * 0  (continuous delivery)
     *
     * @param frequency an int representative of what is desribed above
     * @return this
     */
    public S3Connector deliveryFrequency(int frequency) {
        return setParam("delivery_frequency", String.valueOf(frequency), true);
    }

    /**
     * The maximum amount of data that DataSift will send in a single batch:
     * 102400 (100KB)
     * 256000 (250KB)
     * 512000 (500KB)
     * 1048576 (1MB)
     * 2097152 (2MB)
     * 5242880 (5MB)
     * 10485760 (10MB)
     * 20971520 (20MB)
     *
     * @param maxSize max size as described
     * @return this
     */
    public S3Connector maxSize(int maxSize) {
        return setParam("max_size", String.valueOf(maxSize), true);
    }

    /**
     * @param bucket The bucket within that account into which DataSift will deposit the file
     * @return this
     */
    public S3Connector bucket(String bucket) {
        return setParam("bucket", bucket, true);
    }

    /**
     * optionally set a directory within the configured bucked
     *
     * @return this
     */
    public S3Connector directory(String directory) {
        return setParam("directory", directory, false);
    }

    /**
     * An optional prefix to the filename. Each time DataSift delivers a file, it constructs a name in this format:
     * file_prefix + subscription id + timestamp.json
     *
     * @return this
     */
    public S3Connector filePrefix(String prefix) {
        return setParam("file_prefix", prefix, false);
    }

    /**
     * The access level of the file after it is uploaded to S3:
     * private (Owner-only read/write)
     * public-read (Owner read/write, public read)
     * public-read-write (Public read/write)
     * authenticated-read (Owner read/write, authenticated read)
     * bucket-owner-read (Bucket owner read)
     * bucket-owner-full-control (Bucket owner full control)     * @param acl
     *
     * @return this
     */
    public S3Connector acl(String acl) {
        return setParam("acl", acl, true);
    }

    public static enum S3OutputFormat {
        /**
         * The current default format, where each payload contains a full JSON document. It contains metadata and an
         * "interactions" property that has an array of interactions.
         */
        JSON_META,
        /**
         * The payload is a full JSON document, but just has an array of interactions.
         */
        JSON_ARRAY,
        /**
         * The payload is NOT a full JSON document. Each interaction is flattened and separated by a line break.
         */
        JSON_NEW_LINE
    }
}
