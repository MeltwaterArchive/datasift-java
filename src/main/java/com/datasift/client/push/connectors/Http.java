package com.datasift.client.push.connectors;

import com.datasift.client.push.OutputType;
import io.netty.handler.codec.http.HttpMethod;

/*
 * <a href="http://dev.datasift.com/docs/push/connectors/http">Official docs</a>
 *
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class Http extends BaseConnector<Http> {
    public Http() {
        super(OutputType.HTTP_TYPE);
        setup(this, "method", "url", "delivery_frequency", "max_size", "auth.type", "verify_ssl", "use_gzip");
    }

    /*
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
    public Http format(HttpFormat format) {
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

    /*
     * The verb that you want DataSift to use with the HTTP request:
     * <p/>
     * POST
     * <p/>
     * PUT
     *
     * @return this
     */
    public Http method(HttpMethod method) {
        return setParam("method", method.name());
    }

    /*
     * Any valid URL that you want DataSift to deliver to; for example:
     * http://www.fromdatasift.com/destination/
     * <p/>
     * For POST requests:
     * DataSift uses the URL that you specify.
     * <p/>
     * For PUT requests:
     * DataSift appends a filename to the URL.
     * For example, suppose that you supply this URL:
     * http://www.fromdatasift.com/destination/
     * <p/>
     * Internally we append the filename in this format:  DataSift-<subscription_id>-<time>.json
     * <p/>
     * When you hit the /push.create endpoint for the first time, we make a PUT request to
     * http://www.fromdatasift.com/destination/DataSift-verify-31546216.json (where there isn't a subscription id
     * created yet, so we add "verify" into the file name and 31546216 is the time of test).
     * <p/>
     * When DataSift has data ready for delivery using a PUT request, it sends it to
     * http://www.fromdatasift.com/destination/DataSift-abcdefghij1234579-31546216.json (where the abcdefghij1234579
     * is the subscription id and 31546216 is the time of delivery).
     * Make sure that the URL is properly encoded, otherwise your push/create request will fail. This on-line tool
     * can help you properly encode data you want to use in an HTTP request.
     *
     * @return this
     */
    public Http url(String url) {
        return setParam("url", url);
    }

    /*
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
    public Http deliveryFrequency(int freq) {
        return setParam("delivery_frequency", String.valueOf(freq));
    }

    /*
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
    public Http maxSize(int max) {
        return setParam("max_size", String.valueOf(max));
    }

    /*
     * The authentication that you want DataSift to use when connecting to output_params.url:
     * <p/>
     * basic
     * <p/>
     * none
     * <p/>
     * If you choose basic authentication, you must supply output_params.auth.username and output_params.auth.password.
     * If you specify "none" for authentication, or if you do not include this parameter,
     * DataSift does not check for a username or password.
     *
     * @return this
     */
    public Http authType(AuthType auth) {
        return setParam("auth.type", auth.value());
    }

    /*
     * If you are using SSL to connect, this specifies whether the certificate should be verified. Useful when a
     * client has a self-signed certificate for development. Possible values are:
     * true
     * <p/>
     * false
     *
     * @return this
     */
    public Http verifySSL(boolean trueOrFalse) {
        return setParam("verify_ssl", String.valueOf(trueOrFalse));
    }

    /*
     * The compression setting that you want DataSift to use:
     * true
     * false
     * If you set this parameter to true, DataSift compresses the data using the ZLIB compression standard with the
     * compression level 6. We also add an additional entry to the header:
     * Content-Encoding: gzip
     *
     * @return this
     * @deprecated use {@link #gzip()} OR {@link #zlib()} instead
     */
    public Http useGzip(boolean trueOrFalse) {
        return setParam("use_gzip", String.valueOf(trueOrFalse));
    }

    public Http gzip() {
        return compression("gzip");
    }

    public Http zlib() {
        return compression("zlib");
    }

    public Http compression(String format) {
        return setParam("compression", format);
    }

    /*
     * Sets the authentication information that should be used for the connector
     *
     * @param username the username
     * @param password the password
     * @return this
     */
    public Http auth(String username, String password) {
        return username(username).password(password);
    }

    /*
     * @param username he username for authorization.
     * @return this
     */
    public Http username(String username) {
        return setParam("auth.username", username);
    }

    /*
     * @param password The password for authorization.
     * @return this
     */
    public Http password(String password) {
        return setParam("auth.password", password);
    }

    public Prepared parameters() {
        if ("basic".equals(params.get("auth.type"))
                //if auth type is basic then username and password are required
                && (!params.has("auth.username") || !params.has("auth.password"))) {
            throw new IllegalStateException("Username AND  password required if when the auth.type is basic");
        }
        return super.parameters();
    }

    public static enum HttpFormat {
        JSON_META,
        JSON_ARRAY,
        JSON_NEW_LINE;

        public static HttpFormat fromStr(String str) {
            try {
                return HttpFormat.valueOf(str.toUpperCase());
            } catch (IllegalArgumentException iae) {
                return HttpFormat.valueOf(str);
            }
        }
    }

    public static class AuthType {
        private final String value;

        public AuthType(String value) {
            if (value == null || value.isEmpty()) {
                throw new IllegalArgumentException("Invalid auth type, cannot be null or empty");
            }
            this.value = value;
        }

        public String value() {
            return value;
        }
    }
}
