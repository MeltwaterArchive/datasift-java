package com.datasift.client;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class DataSiftClientTest extends TestUtil {

    @Test(expected = IllegalArgumentException.class)
    public void testClientConstructor() throws Exception {
        new DataSiftClient(null);
    }

    @Test
    public void testHistorics() throws Exception {
        assertNotNull("datasift.historics() should never be null", datasift.historics());
    }

    @Test
    public void testManagedSource() throws Exception {
        assertNotNull("datasift.managedSource() should never be null", datasift.managedSource());
    }

    @Test
    public void testPreview() throws Exception {
        assertNotNull("datasift.preview() should never be null", datasift.preview());
    }

    @Test
    public void testPush() throws Exception {
        assertNotNull("datasift.push() should never be null", datasift.push());
    }

    @Test
    public void testConfig() throws Exception {
        assertNotNull("datasift.config() should never be null", datasift.config());
    }

    @Test
    public void testLiveStream() throws Exception {
        assertNotNull("datasift.liveStream() should never be null", datasift.liveStream());
    }
}
