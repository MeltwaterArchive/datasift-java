package com.datasift.client;

import io.higgs.http.client.HttpRequestBuilder;

import java.net.URI;
import java.net.URISyntaxException;

/**
 */
public class DataSiftConfig {
    private final String illConfigured = "(%s) is null, this is an ill-configured object and all " +
            "API requests using it will fail";
    //TODO, add proxy support for version 3.1.0
    protected String proxyHost;
    protected int proxyPort;
    protected String username, apiKey;
    protected boolean sslEnabled = true;
    protected String host = "api.datasift.com";
    /**
     * This instance should be used as a base for configurations.
     * All new requests should use {@link io.higgs.http.client.HttpRequestBuilder#copy()}
     * Although a single instance would work, this keeps things simple and ensures no stale data is passed in
     * subsequent requests.
     * It is also very cheap in terms of Netty's thread usage because Higgs re-uses the same event loop group
     * so it'll never be creating a whole new load of resources for each instance.
     */
    protected HttpRequestBuilder http = HttpRequestBuilder.instance();
    private String versionPrefix = "v1";
    private String urlEncodingFormat = "ISO-8859-1";

    public DataSiftConfig() {
        http.userAgent("Mozilla/5.0 (compatible; Java Client/3.0.0; +https://github.com/datasift/datasift-java)");
    }

    /**
     * Initialize a new config instance with the given username and api key
     *
     * @param username the DataSift username
     * @param apiKey   the DataSift API key
     */
    public DataSiftConfig(String username, String apiKey) {
        this();
        auth(username, apiKey);
    }

    /**
     * Provide the user credentials that should be used for authentication
     *
     * @param username the DataSift username
     * @param apiKey   the DataSift API key
     * @return this object for further configuration
     */
    public DataSiftConfig auth(String username, String apiKey) {
        if (username == null || apiKey == null || username.isEmpty() || apiKey.isEmpty()) {
            throw new IllegalArgumentException(String.format("A valid username and API key are required. Username = " +
                    "%s, API key = %s", username, apiKey));
        }
        this.username = username;
        this.apiKey = apiKey;
        return this;
    }

    /**
     * @return The HTTP protocol prefix to use.
     *         If SSL is enabled this will be "https://" if not it'll be "http://"
     */
    public String protocol() {
        return sslEnabled ? "https://" : "http://";
    }

    /**
     * @return The host name to which all api calls with this configurations will be made
     */
    public String host() {
        return host;
    }

    /**
     * @return The port on which connections should be made
     */
    public int port() {
        return sslEnabled ? 443 : 80;
    }

    /**
     * @return A base URL to the DataSift API. e.g. https://api.datasift.com/v1/
     */
    public URI baseURL() {
        StringBuilder b = new StringBuilder()
                .append(protocol())
                .append(host())
                .append(":")
                .append(port())
                .append("/")
                .append(versionPrefix())
                .append("/");
        try {
            return new URI(b.toString());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Unable to construct a base URL for the API", e);
        }
    }

    /**
     * Generate a new URI object given an endpoint relative to the base url of this configuration.
     * For example, if the base URL is https://api.datasift.com/v1/ and the endpoint parameters is validate
     * this will return the URI https://api.datasift.com/v1/validate.
     * Technically any path acceptable by {@link URI#resolve(String)} is acceptable
     *
     * @param endpoint the endpoint to return a URI for.
     * @return a new URI with the resolved URL that is safe to manipulate
     */
    public URI newAPIEndpointURI(String endpoint) {
        return baseURL().resolve(endpoint);
    }

    /**
     * Generate a new URI object given an endpoint relative to the base url of this configuration.
     * For example, if the base URL is https://api.datasift.com/v1/ and the endpoint parameters is validate
     * this will return the URI https://api.datasift.com/v1/validate.
     * Technically any path acceptable by {@link URI#resolve(URI)} is acceptable
     *
     * @param endpoint the endpoint to return a URI for.
     * @return a new URI with the resolved URL that is safe to manipulate
     */
    public URI newAPIEndpointURI(URI endpoint) {
        return baseURL().resolve(endpoint);
    }

    /**
     * @return The API version prefix to use, e.g. v1
     */
    public String versionPrefix() {
        return versionPrefix;
    }

    public String getUsername() {
        if (username == null) {
            throw new IllegalStateException(String.format(illConfigured, "Username"));
        }
        return username;
    }

    public String getApiKey() {
        if (username == null) {
            throw new IllegalStateException(String.format(illConfigured, "API key"));
        }
        return apiKey;
    }

    public boolean isSslEnabled() {
        return sslEnabled;
    }

    public void setSslEnabled(boolean sslEnabled) {
        this.sslEnabled = sslEnabled;
    }

    public String authAsHeader() {
        return username + ":" + apiKey;
    }

    public HttpRequestBuilder http() {
        return http;
    }

    /**
     * @return The format that should be used to URL encode prameters
     */
    public String urlEncodingFormat() {
        return urlEncodingFormat;
    }

    /**
     * Sets the format that should be used to encode URL parameters when the option arises
     * e.g. ISO-8859-1 or UTF-8
     *
     * @param format the format
     * @return this
     */
    public DataSiftConfig urlEncodingFormat(String format) {
        urlEncodingFormat = format;
        return this;
    }
}
