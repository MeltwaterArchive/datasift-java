package com.datasift.client;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class DataSiftClientTest {
    private DataSiftClient client;

    @Before
    public void setUp() {
        client = new DataSiftClient(new DataSiftConfig("username", "api-key"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClientConstructor() throws Exception {
        new DataSiftClient(null);
    }

    @Test
    public void testHistorics() throws Exception {
        assertNotNull("client.historics() should never be null", client.historics());
    }

    @Test
    public void testManagedSource() throws Exception {
        assertNotNull("client.managedSource() should never be null", client.managedSource());
    }

    @Test
    public void testCore() throws Exception {
        assertNotNull("client.core() should never be null", client.core());
    }

    @Test
    public void testPreview() throws Exception {
        assertNotNull("client.preview() should never be null", client.preview());
    }

    @Test
    public void testPush() throws Exception {
        assertNotNull("client.push() should never be null", client.push());
    }

    @Test
    public void testConfig() throws Exception {
        assertNotNull("client.config() should never be null", client.config());
    }

    @Test
    public void testLiveStream() throws Exception {
        assertNotNull("client.liveStream() should never be null", client.liveStream());
    }
}
