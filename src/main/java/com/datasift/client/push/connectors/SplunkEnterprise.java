package com.datasift.client.push.connectors;

/**
 * <a href="http://dev.datasift.com/docs/push/connectors/couchdb">Official docs</a>
 *
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class SplunkEnterprise extends BaseConnector<SplunkEnterprise> {
    protected SplunkEnterprise() {
        setup(this, "host", "port", "auth.username", "auth.password");
    }

    public SplunkEnterprise url(String hostname, int port) {
        return host(hostname).port(port);
    }

    public SplunkEnterprise host(String hostname) {
        return setParam("host", hostname);
    }

    public SplunkEnterprise port(int port) {
        return setParam("port", String.valueOf(port));
    }

    public SplunkEnterprise username(String username) {
        return setParam("auth.username", username);
    }

    public SplunkEnterprise password(String password) {
        return setParam("auth.password", password);
    }
}
