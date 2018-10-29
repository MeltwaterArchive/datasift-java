package com.datasift.client.push.connectors;

import com.datasift.client.push.OutputType;

/*
 * <a href="http://dev.datasift.com/docs/push/connectors/elasticsearch">Official docs</a>
 *
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class ElasticSearch extends BaseConnector<ElasticSearch> {
    public ElasticSearch() {
        super(OutputType.ELASTIC_SEARCH);
        setup(this, "host");
    }

    /*
     * Simple calls {@link #host(String)}.{@link #port(int)} for convenience
     *
     * @param host the host of the server
     * @param port the port on which the connection will be attempted
     * @return this
     */
    public ElasticSearch url(String host, int port) {
        return host(host).port(port);
    }

    /*
     * @param host The host name of your CouchDB installation.
     * @return this
     */
    public ElasticSearch host(String host) {
        return setParam("host", host);
    }

    /*
     * @param host You can specify a port or accept the default.
     * @return this
     */
    public ElasticSearch port(int host) {
        return setParam("port", String.valueOf(host));
    }

    /*
     * @param indexName The ElasticSearch index that you want to use.
     *                  If it does not exist, DataSift will create it for you and set its name to the Push
     *                  subscription id.
     *                  Use a valid index name.
     * @return this
     */
    public ElasticSearch index(String indexName) {
        return setParam("index", indexName);
    }

    /*
     * @param type The type that you want to use for the index.
     *             If it does not exist, DataSift will add a new type for the index and use the new type.
     *             Use a valid type name.
     *             For more information on ElasticSearch mapping types,
     *             <a href="http://www.elasticsearch.org/guide/reference/mapping/">read the documentation.</a>
     * @return this
     */
    public ElasticSearch type(String type) {
        return setParam("type", type);
    }

    /*
     * The output format for your data:
     * basic_interaction_meta - The current default format, where each payload contains only basic interaction JSON
     * document.
     * full_interaction_meta - The payload is a full interaction with augmentations.
     * Take a look at our
     * <a href="http://dev.datasift.com/docs/push/sample-output-database-connectors">Sample Output for Database
     * Connectors page.</a>
     *
     * @return this
     */
    public ElasticSearch format(ElasticSearchFormat format) {
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
        return setParam("format", strFormat);
    }

    public static enum ElasticSearchFormat {
        /*
         * The current default format, where each payload contains only basic interaction JSON document.
         */
        BASIC_INTERACTION_META,
        /*
         * The payload is a full interaction with augmentations.
         */
        FULL_INTERACTION_META;

        public static ElasticSearchFormat fromStr(String str) {
            try {
                return ElasticSearchFormat.valueOf(str.toUpperCase());
            } catch (IllegalArgumentException iae) {
                return ElasticSearchFormat.valueOf(str);
            }
        }
    }
}
