package com.datasift.client;

import io.higgs.http.client.HttpRequestBuilder;
import io.higgs.http.client.Request;
import io.higgs.http.client.future.PageReader;
import io.higgs.http.client.future.Reader;
import junit.framework.TestCase;

import java.net.URI;

public class ResponseTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
    }

    public void testCanCreateResponse() throws Exception {
        Reader r = new PageReader();
        Request req = HttpRequestBuilder.instance().GET(new URI("http://localhost/"), r);
        io.higgs.http.client.Response httpRes = new io.higgs.http.client.Response(req, r);
        String data = "{}";
        Response response = new Response(data, httpRes);
        assertEquals(data, response.data());
        assertNotNull(response.rawResponse);
        assertEquals(httpRes, response.rawResponse);
    }

    public void testRequestFailed() throws Exception {
    }
}
