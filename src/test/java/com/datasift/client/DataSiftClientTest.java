package com.datasift.client;

import com.datasift.client.core.Validation;
import com.datasift.client.exceptions.DataSiftException;
import org.junit.Assert;
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
    public void testConfigisNeverNull() throws Exception {
        assertNotNull("datasift.config() should never be null", datasift.config());
    }

    @Test
    public void testLiveStreamisNeverNull() throws Exception {
        assertNotNull("datasift.liveStream() should never be null", datasift.liveStream());
    }

    @Test(expected = DataSiftException.class)
    public void testInvalidCSDL() {
        final DataSiftConfig config = new DataSiftConfig("zcourts", "this-is-wrong");
        final DataSiftClient client = new DataSiftClient(config);
        final String csdl = "tumblr.media exists";
        final Validation validation = client.validate(csdl).sync();
        Assert.assertFalse(validation.isValid());
    }
}
