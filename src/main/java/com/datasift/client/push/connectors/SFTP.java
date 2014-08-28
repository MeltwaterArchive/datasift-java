package com.datasift.client.push.connectors;

import com.datasift.client.push.OutputType;

/**
 * <a href="http://dev.datasift.com/docs/push/connectors/sftp">Official docs</a>
 *
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class SFTP extends BaseConnector<SFTP> {
    public SFTP() {
        super(OutputType.SFTP_OUTPUT);
        setup(this, "host", "port", "directory", "delivery_frequency", "max_size", "auth.username", "auth.password");
    }

    /**
     * Simple calls {@link #host(String)}.{@link #port(int)} for convenience
     *
     * @param host the host of the server
     * @param port the port on which the connection will be attempted
     * @return this
     */
    public SFTP url(String host, int port) {
        return host(host).port(port);
    }

    /**
     * @param host The host name of your CouchDB installation.
     * @return this
     */
    public SFTP host(String host) {
        return setParam("host", host);
    }

    /**
     * @param host You can specify a port or accept the default.
     * @return this
     */
    public SFTP port(int host) {
        return setParam("port", String.valueOf(host));
    }

    /**
     * A directory on the server
     *
     * @return this
     */
    public SFTP directory(String directory) {
        return setParam("directory", directory);
    }

    /**
     * The minimum number of seconds you want DataSift to wait before sending data again:
     * <p/>
     * 10 (10 seconds)
     * <p/>
     * 30 (30 seconds)
     * <p/>
     * 60 (1 minute)
     * <p/>
     * 120 (2 minutes)
     * <p/>
     * 300 (5 minutes)
     * <p/>
     * In reality, a stream might not have data available after the wait. Typically this happens in streams that have
     * very tight filtering constraints, so the wait time might be longer than you specify. If your system is capable
     * of handling large amounts of incoming data, we recommend you use continuous delivery:
     * <p/>
     * 0 (continuous delivery)
     *
     * @return this
     */
    public SFTP deliveryFrequency(int freq) {
        return setParam("delivery_frequency", String.valueOf(freq));
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
     * @return this
     */
    public SFTP maxSize(int max) {
        return setParam("max_size", String.valueOf(max));
    }

    /**
     * An optional prefix to the filename. Each time Datasift delivers a file, it constructs a name in this format:
     * file_prefix + subscription id + timestamp.json
     *
     * @return this
     */
    public SFTP filePrefix(String prefix) {
        return setParam("file_prefix", prefix);
    }

    /**
     * Sets the authentication information that should be used for the connector
     *
     * @param username the username
     * @param password the password
     * @return this
     */
    public SFTP auth(String username, String password) {
        return username(username).password(password);
    }

    /**
     * @param username he username for authorization.
     * @return this
     */
    public SFTP username(String username) {
        return setParam("auth.username", username);
    }

    /**
     * @param password The password for authorization.
     * @return this
     */
    public SFTP password(String password) {
        return setParam("auth.password", password);
    }

    /**
     * This enables you to see which files are being written and which are complete. Possible values are:
     * <p/>
     * true
     * <p/>
     * false
     * <p/>
     * If you omit this parameter, it defaults to false. If you set it to true, we append ".part" to the filename
     * while we're writing to it. Once the transfer is complete, we remove ".part".
     *
     * @return this
     */
    public SFTP markInProgress(boolean trueOrFalse) {
        return setParam("mark_in_progress", String.valueOf(trueOrFalse));
    }

    public SFTP gzip() {
        return compression("gzip");
    }

    public SFTP compression(String format) {
        return setParam("compression", format);
    }

    /**
     * The output format for your data:
     * <p/>
     * json_meta - The current default format, where each payload contains a full JSON document. It contains metadata
     * and an "interactions" property that has an array of interactions.
     * <p/>
     * json_array - The payload is a full JSON document, but just has an array of interactions.
     * <p/>
     * json_new_line - The payload is NOT a full JSON document. Each interaction is flattened and separated by a line
     * break.
     * If you omit this parameter or set it to json_meta, your output consists of JSON metadata followed by a JSON
     * array of interactions (wrapped in square brackets and separated by commas).
     * Take a look at our Sample Output for File-Based Connectors page.
     * If you select json_array, DataSift omits the metadata and sends just the array of interactions.
     * If you select json_new_line, DataSift omits the metadata and sends each interaction as a single JSON object.
     *
     * @return this
     */
    public SFTP format(SFTPFormat format) {
        String strFormat;
        switch (format) {
            case JSON_ARRAY:
                strFormat = "json_array";
                break;
            case JSON_NEW_LINE:
                strFormat = "json_new_line";
                break;
            default:
            case JSON_META:
                strFormat = "json_meta";
                break;
        }
        return setParam("format", strFormat);
    }

    public static enum SFTPFormat {
        JSON_META,
        JSON_ARRAY,
        JSON_NEW_LINE;

        public static SFTPFormat fromStr(String str) {
            try {
                return SFTPFormat.valueOf(str.toUpperCase());
            } catch (IllegalArgumentException iae) {
                return SFTPFormat.valueOf(str);
            }
        }
    }
}
