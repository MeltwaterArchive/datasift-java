package com.datasift.client.managedsource.sources;

import com.datasift.client.DataSiftConfig;
import com.datasift.client.managedsource.ManagedDataSourceType;

/*
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class Yammer extends BaseSource<Yammer> {
    public Yammer(DataSiftConfig config) {
        super(config, ManagedDataSourceType.YAMMER);
    }

    /*
     * if true links augmentations will be applied to URLs found via Yammer.
     *
     * @return this
     */
    public Yammer extractLinks(boolean enabled) {
        return setParametersField("extract_links", enabled);
    }

    /*
     * Adds an OAuth token to the managed source
     *
     * @param oAuthAccessToken an oauth2 token
     * @param name             a human friendly name for this auth token
     * @param expires          identity resource expiry date/time as a UTC timestamp, i.e. when the token expires
     * @return this
     */
    public Yammer addOAutToken(String oAuthAccessToken, long expires, String name) {
        if (oAuthAccessToken == null || oAuthAccessToken.isEmpty()) {
            throw new IllegalArgumentException("A valid OAuth and refresh token is required");
        }
        AuthParams parameterSet = newAuthParams(name, expires);
        parameterSet.set("value", oAuthAccessToken);
        return this;
    }
}
