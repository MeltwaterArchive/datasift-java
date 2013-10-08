package com.datasift.client.preview;

import com.datasift.client.TestUtil;
import com.datasift.client.core.Stream;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class DataSiftPreviewTest extends TestUtil {
    protected HistoricsPreview preview;

    @Before
    public void testCreate() throws Exception {
        Stream stream = createStream();
        preview = datasift.preview().create(DateTime.now().minusHours(3), DateTime.now().minusHours(2), stream,
                new String[]{ "interaction.author.link,targetVol,hour;interaction.type,freqDist,10" }).sync();
        successful(preview);
    }

    @Test
    public void testGet() throws Exception {
        HistoricsPreviewData data = datasift.preview().get(preview).sync();
        successful(data);
    }

    //simple verification tests
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithDate() throws Exception {
        datasift.preview().create(null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithTooManyParams() throws Exception {
        //max of 20 params allowed
        datasift.preview().create(0, 0, "random-hash", new String[21]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithTooFewParams() throws Exception {
        //min of 1 params allowed
        datasift.preview().create(0, 0, "random-hash", new String[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithStringHash() throws Exception {
        String hash = null;
        datasift.preview().create(0, 0, hash, new String[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNullStream() throws Exception {
        Stream hash = null;
        datasift.preview().create(0, 0, hash, new String[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithStringId() throws Exception {
        String id = null;
        datasift.preview().get(id);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithPreview() throws Exception {
        HistoricsPreview id = null;
        datasift.preview().get(id);
    }
}
