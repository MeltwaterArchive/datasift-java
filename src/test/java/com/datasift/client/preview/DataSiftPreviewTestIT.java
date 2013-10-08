package com.datasift.client.preview;

import com.datasift.client.TestUtil;
import com.datasift.client.core.Stream;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class DataSiftPreviewTestIT extends TestUtil {
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
}
