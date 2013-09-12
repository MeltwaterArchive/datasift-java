package com.datasift.client.push.connectors;

import com.datasift.client.push.OutputType;

/**
 * <a href="http://dev.datasift.com/docs/push/connectors/mongodb">Official docs</a>
 *
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class MongoDB extends BaseConnector<MongoDB> {
    public MongoDB() {
        super(OutputType.MONGO_DB);
        setup(this, "host", "db_name", "auth.username", "auth.password");
    }

    /**
     * Simple calls {@link #host(String)}.{@link #port(int)} for convenience
     *
     * @param host the host of the server
     * @param port the port on which the connection will be attempted
     * @return this
     */
    public MongoDB url(String host, int port) {
        return host(host).port(port);
    }

    /**
     * @param host The host name of your CouchDB installation.
     * @return this
     */
    public MongoDB host(String host) {
        return setParam("host", host);
    }

    /**
     * @param host You can specify a port or accept the default.
     * @return this
     */
    public MongoDB port(int host) {
        return setParam("port", String.valueOf(host));
    }

    /**
     * @param name The name of an existing database.
     * @return this
     */
    public MongoDB dbName(String name) {
        return setParam("db_name", name);
    }

    /**
     * Optional collection name. When not specified, DataSift will set the name to DataSift_<subscription_id>. For
     * example, 'DataSift_737c7b5f6f19e49f937356275dfd1a79'
     *
     * @return this
     */
    public MongoDB collectionName(String name) {
        return setParam("collection_name", name);
    }

    /**
     * Sets the authentication information that should be used for the connector
     *
     * @param username the username
     * @param password the password
     * @return this
     */
    public MongoDB auth(String username, String password) {
        return username(username).password(password);
    }

    /**
     * @param username he username for authorization.
     * @return this
     */
    public MongoDB username(String username) {
        return setParam("auth.username", username);
    }

    /**
     * @param password The password for authorization.
     * @return this
     */
    public MongoDB password(String password) {
        return setParam("auth.password", password);
    }

    /**
     * The output format for your data:
     * basic_interaction_meta - The current default format, where each payload contains only basic interaction JSON
     * document.
     * full_interaction_meta - The payload is a full interaction with augmentations.
     * Take a look at our Sample Output for Database Connectors page.
     *
     * @return this
     */
    public MongoDB format(MongoDBFormat format) {
        String strFormat;
        switch (format) {
            case BASIC_INTERACTION_META:
                strFormat = "basic_interaction_meta";
                break;
            case FULL_INTERACTION_META_DATE:
                strFormat = "full_interaction_meta_date";
                break;
            default:
            case FULL_INTERACTION_META:
                strFormat = "full_interaction_meta";
                break;
        }
        return setParam("format", strFormat);
    }

    public static enum MongoDBFormat {
        /**
         * The current default format, where each payload contains only basic interaction JSON document.
         */
        BASIC_INTERACTION_META,
        /**
         * The payload is a full interaction with augmentations.
         */
        FULL_INTERACTION_META,
        /**
         * Each payload is a full interaction with augmentations and a MongoDB-specific date representation.
         */
        FULL_INTERACTION_META_DATE
    }
}
