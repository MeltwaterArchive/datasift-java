package com.datasift.client;

import io.higgs.http.client.HttpRequestBuilder;
import io.higgs.http.client.Request;
import io.higgs.http.client.future.PageReader;
import io.higgs.http.client.future.Reader;
import junit.framework.TestCase;

import java.net.URI;

/**
 * Created by agnieszka on 10/01/2014.
 */
public class ResponseTest extends TestCase {
    public void setUp() throws Exception {
        super.setUp();

    }

    public void tearDown() throws Exception {

    }

    public void testHasFailed() throws Exception {

    }

    public void testFailureCause() throws Exception {

    }

    public void testStatus() throws Exception {

    }

    public void testStatusMessage() throws Exception {

    }

    public void testProtocolVersion() throws Exception {

    }

    public void testIsChunked() throws Exception {

    }

    public void testData() throws Exception {

    }

    public void testHeaders() throws Exception {

    }

    public void testToString() throws Exception {

    }

    public void testCanCreateResponse() throws Exception {
        Reader r = new PageReader();
        Request req = HttpRequestBuilder.instance().GET(new URI("http://localhost/"), r);
        io.higgs.http.client.Response httpRes = new io.higgs.http.client.Response(req, r);
        String data = "{}";
        Response response = new Response(data, httpRes);
        assertEquals(data,response.data());
        assertNotNull(response.rawResponse);
        assertEquals(httpRes, response.rawResponse);
    }

    public void testRequestFailed() throws Exception {

    }
}
