package com.datasift.client.push.connectors;

import com.datasift.client.push.OutputType;

/**
 * <a href="http://dev.datasift.com/docs/push/connectors/couchdb">Official docs</a>
 *
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class SplunkStormRest extends BaseConnector<SplunkStormRest> {
    public SplunkStormRest() {
        super(OutputType.SPLUNK_STORM_REST);
        setup(this, "api_hostname", "project_id", "access_token");
    }

    public SplunkStormRest apiHostname(String hostname) {
        return setParam("api_hostname", hostname);
    }

    public SplunkStormRest projectId(String id) {
        return setParam("project_id", id);
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
    public SplunkStormRest format(SplunkStormRestFormat format) {
        String strFormat;
        switch (format) {
            case JSON_NEW_LINE_TIMESTAMP:
                strFormat = "json_new_line";
                break;
            default:
            case JSON_NEW_LINE_TIMESTAMP_META:
                strFormat = "json_new_line_meta";
                break;
        }
        return setParam("format", strFormat);
    }

    /**
     * @param token he username for authorization.
     * @return this
     */
    public SplunkStormRest accessToken(String token) {
        return setParam("auth.access_token", token);
    }

    public static enum SplunkStormRestFormat {
        /**
         * Each interaction is sent separately except it is framed with metadata and an extra timestamp field.
         */
        JSON_NEW_LINE_TIMESTAMP_META,
        /**
         * Each interaction is sent separately and has an extra timestamp property and no meta data.
         */
        JSON_NEW_LINE_TIMESTAMP
    }
}
