package com.datasift.client.push.destinations;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class CouchDBConnector extends BaseConnector<CouchDBConnector> {
    protected CouchDBConnector() {
        setup(this);
    }

    /**
     * Simple calls {@link #host(String)}.{@link #port(int)} for convenience
     *
     * @param host the host of the server
     * @param port the port on which the connection will be attempted
     * @return this
     */
    public CouchDBConnector url(String host, int port) {
        return host(host).port(port);
    }

    /**
     * @param host The host name of your CouchDB installation.
     * @return this
     */
    public CouchDBConnector host(String host) {
        return setParam("host", host, true);
    }

    /**
     * @param host You can specify a port or accept the default.
     * @return this
     */
    public CouchDBConnector port(int host) {
        return setParam("port", String.valueOf(host), true);
    }

    /**
     * @param name The name of an existing database.
     * @return this
     */
    public CouchDBConnector dbName(String name) {
        return setParam("db_name", name, true);
    }

    /**
     * Sets the authentication information that should be used for the connector
     *
     * @param username the username
     * @param password the password
     * @return this
     */
    public CouchDBConnector auth(String username, String password) {
        return username(username).password(password);
    }

    /**
     * @param username he username for authorization.
     * @return this
     */
    public CouchDBConnector username(String username) {
        return setParam("auth.username", username, true);
    }

    /**
     * @param password The password for authorization.
     * @return this
     */
    public CouchDBConnector password(String password) {
        return setParam("auth.password", password, true);
    }

    /**
     * Whether SSL should be used when connecting to the database. Possible values are:
     * yes
     * no
     *
     * @return this
     */
    public CouchDBConnector useSSL(String yesOrNo) {
        if (yesOrNo == null || !"yes".equals(yesOrNo) || !"no".equals(yesOrNo)) {
            throw new IllegalArgumentException("The strings yes or no are the only valid options for the use ssl " +
                    "option");
        }
        return setParam("use_ssl", yesOrNo, true);
    }

    /**
     * If you are using SSL to connect, this specifies whether the certificate should be verified. Useful when a
     * client has a self-signed certificate for development. Possible values are:
     * yes
     * no
     *
     * @return this
     */
    public CouchDBConnector verifySSL(String yesOrNo) {
        if (yesOrNo == null || !"yes".equals(yesOrNo) || !"no".equals(yesOrNo)) {
            throw new IllegalArgumentException("The strings yes or no are the only valid options for the veirfy ssl " +
                    "option");
        }
        return setParam("verify_ssl", yesOrNo, false);
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
    public CouchDBConnector format(CouchDBFormat format) {
        String strFormat;
        switch (format) {
            case FULL_INTERACTION_META:
                strFormat = "full_interaction_meta";
                break;
            default:
            case BASIC_INTERACTION_META:
                strFormat = "basic_interaction_meta";
                break;
        }
        return setParam("format", strFormat, false);
    }

    public static enum CouchDBFormat {
        /**
         * The current default format, where each payload contains only basic interaction JSON document.
         */
        BASIC_INTERACTION_META,
        /**
         * The payload is a full interaction with augmentations.
         */
        FULL_INTERACTION_META
    }
}
