package com.datasift.client;

/**
 */
public class DataSiftConfig {
    //TODO, add proxy support for version 3.1.0
    protected String proxyHost;
    protected int proxyPort;
    protected String username, apiKey;
    protected boolean sslEnabled = true;
    private final String illConfigured = "(%s) is null, this is an ill-configured object and all " +
            "API requests using it will fail";

    public DataSiftConfig() {
    }

    /**
     * Initialize a new config instance with the given username and api key
     *
     * @param username the DataSift username
     * @param apiKey   the DataSift API key
     */
    public DataSiftConfig(String username, String apiKey) {
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
}
