package com.datasift.client;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class DataSiftClientTest extends TestUtil {

    @Test(expected = IllegalArgumentException.class)
    public void testClientConstructorDoesntAcceptNullConfig() throws Exception {
        new DataSiftClient(null);
    }

    @Test
    public void testHistoricsisNeverNull() throws Exception {
        assertNotNull("datasift.historics() should never be null", datasift.historics());
    }

    @Test
    public void testManagedSourceisNeverNull() throws Exception {
        assertNotNull("datasift.managedSource() should never be null", datasift.managedSource());
    }

    @Test
    public void testPreviewisNeverNull() throws Exception {
        assertNotNull("datasift.preview() should never be null", datasift.preview());
    }

    @Test
    public void testPushisNeverNull() throws Exception {
        assertNotNull("datasift.push() should never be null", datasift.push());
    }

    @Test
    public void testODPisNeverNull() throws Exception {
        assertNotNull("datasift.odp() should never be null", datasift.odp());
    }

    @Test
    public void testConfigisNeverNull() throws Exception {
        assertNotNull("datasift.config() should never be null", datasift.config());
    }

    @Test
    public void testLiveStreamisNeverNull() throws Exception {
        assertNotNull("datasift.liveStream() should never be null", datasift.liveStream());
    }

    @Test
    public void testPylonisNeverNull() throws Exception {
        assertNotNull("datasift.pylon() should never be null", datasift.pylon());
    }
}
