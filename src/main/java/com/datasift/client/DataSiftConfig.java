package com.datasift.client;

import io.higgs.http.client.HttpRequestBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 */
public class DataSiftConfig {
    private static final String illConfigured = "(%s) is null, this is an ill-configured object and all " +
            "API requests using it will fail";
    protected boolean compatibleSSLProtocolsFound;
    protected List<String> sslProtocols = new ArrayList<>();
    protected String username, apiKey;
    protected boolean sslEnabled = true;
    protected String host = "api.datasift.com";
    protected String wsHost = "websocket.datasift.com";
    protected String ingestionHost = "in.datasift.com";

    /**
     * This instance should be used as a base for configurations.
     * All new requests should use {@link io.higgs.http.client.HttpRequestBuilder#copy()}
     * Although a single instance would work, this keeps things simple and ensures no stale data is passed in
     * subsequent requests.
     * It is also very cheap in terms of Netty's thread usage because Higgs re-uses the same event loop group
     * so it'll never be creating a whole new load of resources for each instance.
     */
    protected HttpRequestBuilder http = HttpRequestBuilder.instance();
    protected String versionPrefix = "v1.6";
    protected String urlEncodingFormat = "ISO-8859-1";
    protected int port = 80;
    protected boolean manualPort;
    private boolean autoReconnect = true;
    protected int connectTimeout = 10000;

    public DataSiftConfig() {
        http.userAgent("DataSift/" + versionPrefix + " Java/" + getClientVersion());

        if (HttpRequestBuilder.isSupportedSSLProtocol("SSLv3")) {
            sslProtocols.add("SSLv3");
        }
        if (HttpRequestBuilder.isSupportedSSLProtocol("TLSv1")) {
            sslProtocols.add("TLSv1");
        }
        if (HttpRequestBuilder.isSupportedSSLProtocol("TLSv1.2")) {
            sslProtocols.add("TLSv1.2");
        }
        compatibleSSLProtocolsFound = sslProtocols.size() > 0;
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
     * Sets a host and port for a proxy that all requests should be sent to
     *
     * @param host proxy host
     * @param port proxy port
     * @return this
     */
    public DataSiftConfig proxy(String host, int port) {
        http.proxy(host, port);
        return this;
    }

    /**
     * @param username the username the proxy requires
     * @param password the password the proxy requires
     * @return this
     */
    public DataSiftConfig proxy(String host, int port, String username, String password) {
        http.proxy(host, port, username, password);
        return this;
    }

    /**
     * @return The HTTP protocol prefix to use.
     * If SSL is enabled this will be "https://" if not it'll be "http://"
     */
    public String protocol() {
        return sslEnabled ? "https://" : "http://";
    }

    public DataSiftConfig host(String host) {
        this.host = host;
        return this;
    }

    /**
     * @return The host name to which all api calls with this configurations will be made
     */
    public String host() {
        return host;
    }

    public DataSiftConfig wsHost(String host) {
        this.wsHost = host;
        return this;
    }

    /**
     * @return The host name used to stream
     */
    public String wsHost() {
        return wsHost;
    }

    public DataSiftConfig ingestionHost(String host) {
        this.ingestionHost = host;
        return this;
    }

    /**
     * @return The host name to which all ingestion api calls with this configurations will be made
     */
    public String ingestionHost() {
        return ingestionHost;
    }

    /**
     * @return The port on which connections should be made
     */
    public int port() {
        return manualPort ? port : sslEnabled ? 443 : 80;
    }

    public void port(int p) {
        manualPort = true;
        port = p;
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
     * @return A base URL to the DataSift Ingestion API. e.g. https://in.datasift.com/
     */
    public URI baseIngestionURL() {
        StringBuilder b = new StringBuilder()
                .append(protocol())
                .append(ingestionHost())
                .append(":")
                .append(port())
                .append("/");
        try {
            return new URI(b.toString());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Unable to construct a base URL for the ingestion API", e);
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
     * Generate a new URI object given an endpoint relative to the base ingestion url of this configuration.
     * For example, if the base URL is https://in.datasift.com/ and the endpoint is the source ID,
     * this would return a URI similar to https://in.datasift.com/9b101935be2044bb9cfdffbc953924e8.
     * Technically any path acceptable by {@link URI#resolve(String)} is acceptable
     *
     * @param endpoint the endpoint to return a URI for.
     * @return a new URI with the resolved URL that is safe to manipulate
     */
    public URI newIngestionAPIEndpointURI(String endpoint) {
        return baseIngestionURL().resolve(endpoint);
    }

    /**
     * Generate a new URI object given an endpoint relative to the base ingestion url of this configuration.
     * For example, if the base URL is https://in.datasift.com/ and the endpoint is the source ID,
     * this would return a URI similar to https://in.datasift.com/9b101935be2044bb9cfdffbc953924e8.
     * Technically any path acceptable by {@link URI#resolve(String)} is acceptable
     *
     * @param endpoint the endpoint to return a URI for.
     * @return a new URI with the resolved URL that is safe to manipulate
     */
    public URI newIngestionAPIEndpointURI(URI endpoint) {
        return baseIngestionURL().resolve(endpoint);
    }

    /**
     * @return The API version prefix to use, e.g. v1
     */
    public String versionPrefix() {
        return versionPrefix;
    }

    /**
     * Force the client to use a version other than the default.
     * @param prefix the prefix to use, this should be along the lines of v1.6 i.e. vMajor.Minor
     */
    public DataSiftConfig versionPrefix(String prefix) {
        versionPrefix = prefix;
        return this;
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
        if (!manualPort) {
            port = this.sslEnabled ? 443 : 80;
        }
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

    public boolean isAutoReconnect() {
        return autoReconnect;
    }

    /**
     * Sets whether streams are automatically reconnected when a websocket connection is closed
     *
     * @param autoReconnect true or false, defaults to true
     */
    public void setAutoReconnect(boolean autoReconnect) {
        this.autoReconnect = autoReconnect;
    }

    /**
     * Gets an array of compatible SSL protocols found on this JVM
     *
     * @return an array of SSL protocols or NULL if none are available
     */
    public String[] sslProtocols() {
        return compatibleSSLProtocolsFound() ? sslProtocols.toArray(new String[sslProtocols.size()]) : null;
    }

    /**
     * @return true if at least one compatible secure protocol is available
     */
    public boolean compatibleSSLProtocolsFound() {
        return compatibleSSLProtocolsFound;
    }

    public int connectTimeout() {
        return connectTimeout;
    }

    public void connectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public String getClientVersion() {
        String path = "/version.prop";
        InputStream stream = getClass().getResourceAsStream(path);
        if (stream == null) {
            return "3.x";
        }
        Properties props = new Properties();
        try {
            props.load(stream);
            stream.close();
            return (String) props.get("version");
        } catch (IOException e) {
            return "3.x";
        }
    }
}
