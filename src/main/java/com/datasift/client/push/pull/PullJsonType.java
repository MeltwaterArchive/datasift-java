package com.datasift.client.push.pull;

import com.datasift.client.push.PullReader;

/**
 * For detailed documentation and example on what each of these are please see
 * http://dev.datasift.com/docs/push/sample-output-file-based-connectors
 *
 * @author Courtney Robinson <courtney@crlog.info>
 */
public enum PullJsonType {
    /**
     * Produce JSON of interaction objects
     */
    JSON_ARRAY,
    /**
     * Produce JSON objects where "interactions" is a JSON array and one of several fields
     */
    JSON_META,
    /**
     * Product a single interaction per line.
     */
    JSON_NEW_LINE;

    /**
     * @param str the string to try and match to a known type
     * @return the type which matches the given string or null if none do
     */
    public PullJsonType fromString(String str) {
        if ("json_array".equalsIgnoreCase(str)) {
//            return JSON_ARRAY;
        } else if ("json_meta".equalsIgnoreCase(str)) {
            return JSON_META;
        } else if ("json_new_line".equalsIgnoreCase(str)) {
            return JSON_NEW_LINE;
        }
        return null;
    }

    public String asString() {
        switch (this) {
//            case JSON_ARRAY:
//                return PullReader.FORMAT_ARRAY.toLowerCase();
            case JSON_META:
                return PullReader.FORMAT_META.toLowerCase();
            case JSON_NEW_LINE:
                return PullReader.FORMAT_NEW_LINE.toLowerCase();
            default:
                return null;
        }
    }
}
