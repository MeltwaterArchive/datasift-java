package com.datasift.client.push.connectors;

import com.datasift.client.push.OutputType;

/*
 * <a href="http://dev.datasift.com/docs/push/connectors/zoomdata">Official docs</a>
 *
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class ZoomData extends BaseConnector<ZoomData> {
    public ZoomData() {
        super(OutputType.ZOOM_DATA);
        setup(this, "host", "port", "auth.username", "auth.password");
    }

    public ZoomData url(String hostname, int port) {
        return host(hostname).port(port);
    }

    public ZoomData host(String hostname) {
        return setParam("host", hostname);
    }

    public ZoomData port(int port) {
        return setParam("port", String.valueOf(port));
    }

    public ZoomData username(String username) {
        return setParam("auth.username", username);
    }

    public ZoomData password(String password) {
        return setParam("auth.password", password);
    }

    /*
     * The label used to mark data delivered to Zoomdata.
     * <p/>
     * This will be used to label data sets in the Zoomdata interface and database.
     * Example value: DataSift
     *
     * @return this
     */
    public ZoomData source(String source) {
        return setParam("source", source);
    }

}
