package com.datasift.client.util;

import com.datasift.client.Response;
import io.higgs.http.client.HttpRequestBuilder;
import io.higgs.http.client.future.PageReader;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class WrappedResponse extends Response {
    private static URI DUMMY_URL;

    static {
        try {
            DUMMY_URL = new URI("http://localhost/wrapped-response");
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Invalid dumy URL????");
        }
    }

    public WrappedResponse() {
        super("", new io.higgs.http.client.Response(HttpRequestBuilder.instance().GET(DUMMY_URL, new PageReader()),
                new PageReader()));
    }

    public boolean hasFailed() {
        return false;
    }

    public Throwable failureCause() {
        return null;
    }

    public int status() {
        return 200;
    }

    public String statusMessage() {
        return "OK";
    }

    public float protocolVersion() {
        return 1.1f;
    }

    public boolean isChunked() {
        return false;
    }

    public String data() {
        return "";
    }

    public Map<String, List<String>> headers() {
        return new HashMap<String, List<String>>();
    }

    public String toString() {
        return "Wrapped response, has no real data";
    }
}
