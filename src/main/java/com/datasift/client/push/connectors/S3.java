package com.datasift.client.push.connectors;


import com.datasift.client.push.OutputType;

/**
 * <a href="http://dev.datasift.com/docs/push/connectors/s3">Official docs</a>
 *
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class S3 extends BaseConnector<S3> {
    public S3() {
        super(OutputType.S3_OUTPUT);
        setup(this, "auth.access_key", "auth.secret_key", "delivery_frequency", "max_size", "bucket", "acl");
    }

    /**
     * Sets the output format for your data
     *
     * @param format one of the allowed S3 formats, defaults to json_meta
     * @return this
     */
    public S3 format(S3OutputFormat format) {
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
        setParam("format", strFormat);
        return this;
    }

    /**
     * @param key The access key for the S3 account that DataSift will send to. Make sure that this value is properly
     *            encoded, otherwise your /push/create request will fail.
     * @return this
     */
    public S3 accessKey(String key) {
        return setParam("auth.access_key", key);
    }

    /**
     * @param secret The secret key for the S3 account that DataSift will send to.     Make sure that this value is
     *               properly encoded, otherwise your /push/create request will fail.
     * @return this
     */
    public S3 secretKey(String secret) {
        return setParam("auth.secret_key", secret);
    }

    /**
     * The minimum number of seconds you want DataSift to wait before sending data again:
     * 0 (continuous delivery)
     * 10 (10 seconds)
     * 30 (30 seconds)
     * 60 (1 minute)
     * 300 (5 minutes)
     * In reality, a stream might not have data available after the wait. Typically this happens in streams that have
     * very tight filtering constraints, so the wait time might be longer than you specify. Amazon's S3 is capable
     * of handling large amounts of incoming data, so we recommend you use continuous delivery.
     *
     * @param frequency an int representative of what is desribed above
     * @return this
     */
    public S3 deliveryFrequency(int frequency) {
        return setParam("delivery_frequency", String.valueOf(frequency));
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
     * 52428800 (50MB)
     * 104857600 (100MB)
     * 209715200 (200MB)
     *
     * @param maxSize max size as described
     * @return this
     */
    public S3 maxSize(int maxSize) {
        return setParam("max_size", String.valueOf(maxSize));
    }

    /**
     * @param bucket The bucket within that account into which DataSift will deposit the file
     * @return this
     */
    public S3 bucket(String bucket) {
        return setParam("bucket", bucket);
    }

    /**
     * optionally set a directory within the configured bucked
     *
     * @return this
     */
    public S3 directory(String directory) {
        return setParam("directory", directory);
    }

    /**
     * An optional prefix to the filename. Each time DataSift delivers a file, it constructs a name in this format:
     * file_prefix + subscription id + timestamp.json
     *
     * @return this
     */
    public S3 filePrefix(String prefix) {
        return setParam("file_prefix", prefix);
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
    public S3 acl(String acl) {
        return setParam("acl", acl);
    }

    public S3 gzip() {
        return compression("gzip");
    }

    public S3 compression(String format) {
        return setParam("compression", format);
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
        JSON_NEW_LINE;

        public static S3OutputFormat fromStr(String str) {
            try {
                return S3OutputFormat.valueOf(str.toUpperCase());
            } catch (IllegalArgumentException iae) {
                return S3OutputFormat.valueOf(str);
            }
        }
    }
}
