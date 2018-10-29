package com.datasift.client.push.connectors;

import com.datasift.client.push.OutputType;

/*
 * <a href="http://dev.datasift.com/docs/push/connectors/redis">Official docs</a>
 *
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class Redis extends BaseConnector<Redis> {
    public Redis() {
        super(OutputType.REDIS);
        setup(this, "host", "port", "database", "list", "format", "auth.password");
    }

    /*
     * The name of the Redis host that DataSift will connect to.
     *
     * @return this
     */
    public Redis host(String host) {
        return setParam("host", host);
    }

    /*
     * The port that you want to use on your server.
     *
     * @return this
     */
    public Redis port(int port) {
        return setParam("port", String.valueOf(port));
    }

    /*
     * The numeric id of an existing database.
     *
     * @return this
     */
    public Redis database(String database) {
        return setParam("database", database);
    }

    /*
     * The name of a list that stores interactions. The list does not have to exist, it will be created if necessary.
     *
     * @return this
     */
    public Redis list(String list) {
        return setParam("list", list);
    }

    /*
     * The output format for your data:
     * json_interaction_meta - This is specific to the Redis connector for now. Each interaction is sent separately
     * except it is framed with metadata.
     * json_interaction - This is specific to the Redis connector for now. Each interaction is sent separately with
     * no metadata.
     * If you omit this parameter or set it to json_interaction_meta, each interaction will be delivered  with
     * accompanying metadata. Both the interactions and the metadata are delivered as JSON objects.
     * <p/>
     * Take a look at our Sample Output for File-Based Connectors page.
     * <p/>
     * If you select json_interaction, DataSift omits the metadata and sends each interaction as a single JSON object.
     *
     * @return this
     */
    public Redis format(RedisFormat format) {
        String strFormat;
        switch (format) {
            case JSON_INTERACTION_META:
                strFormat = "json_interaction_meta";
                break;
            default:
            case JSON_INTERACTION:
                strFormat = "json_interaction";
                break;
        }
        return setParam("format", strFormat);
    }

    /*
     * The password for authentication.
     *
     * @return this
     */
    public Redis password(String password) {
        return setParam("auth.password", password);
    }

    public static enum RedisFormat {
        /*
         * The current default format, where each payload contains only basic interaction JSON document.
         */
        JSON_INTERACTION,
        /*
         * The payload is a full interaction with augmentations.
         */
        JSON_INTERACTION_META;

        public static RedisFormat fromStr(String str) {
            try {
                return RedisFormat.valueOf(str.toUpperCase());
            } catch (IllegalArgumentException iae) {
                return RedisFormat.valueOf(str);
            }
        }
    }
}
