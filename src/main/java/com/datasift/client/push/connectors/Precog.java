package com.datasift.client.push.connectors;

import com.datasift.client.push.OutputType;

/*
 * <a href="http://dev.datasift.com/docs/push/connectors/precog">Official docs</a>
 *
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class Precog extends BaseConnector<Precog> {
    public Precog() {
        super(OutputType.PRECOG);
        setup(this, "domain", "path", "owner_id", "auth.api_key");
    }

    /*
     * @param domain The URL of the Precog API host.
     *               <p/>
     *               Example values: http://beta.precog.com
     * @return this
     */
    public Precog domain(String domain) {
        return setParam("domain", domain);
    }

    /*
     * @param path The path to the data directory.
     *             <p/>
     *             Example values: /1234567890/
     * @return this
     */
    public Precog path(String path) {
        return setParam("path", path);
    }

    /*
     * Your or other Precog user's Owner ID. It is possible to deliver interactions to another Precog user's account
     * if you know their Owner ID.
     * <p/>
     * Example values: 1234567890
     *
     * @return this
     */
    public Precog ownerId(String name) {
        return setParam("owner_id", name);
    }

    /*
     * Your Precog API key.
     * <p/>
     * Example values: ABCDEFGH-0000-0000-0000-000000000000
     *
     * @return this
     */
    public Precog apiKey(String apiKey) {
        return setParam("auth.api_key", apiKey);
    }
}
