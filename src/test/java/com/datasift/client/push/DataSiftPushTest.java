package com.datasift.client.push;

import com.datasift.client.DataSiftClient;
import com.datasift.client.DataSiftConfig;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class DataSiftPushTest {
    private DataSiftClient client;

    @Before
    public void setUp() {
        client = new DataSiftClient(new DataSiftConfig("username", "api-key"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPause() throws Exception {
        client.push().pause(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResume() throws Exception {
        client.push().resume(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStop() throws Exception {
        client.push().stop(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDelete() throws Exception {
        client.push().delete(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWithConnector() throws Exception {
        client.push().update(null, PushConnectors.bigQuery());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWithId() throws Exception {
        client.push().update("andom-id", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPull() throws Exception {
        client.push().pull(null, 10, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithId() throws Exception {
        client.push().get(null);
    }
}
