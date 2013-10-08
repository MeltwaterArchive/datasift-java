package com.datasift.client.historics;

import com.datasift.client.DataSiftResult;
import com.datasift.client.FutureData;
import com.datasift.client.TestUtil;
import com.datasift.client.core.Stream;
import com.datasift.client.push.PushSubscription;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class HistoricsTestIT extends TestUtil {

    protected PushSubscription subscription;
    private Stream stream;
    private PreparedHistoricsQuery query;

    public HistoricsTestIT() throws Exception {
        super.setup();
    }

    @Before
    public void setup() throws Exception {
        stream = createStream();
    }

    @Test
    public void prepareAndStart() throws InterruptedException {
        DateTime fiveHrsAgo = DateTime.now().minusHours(5);
        DateTime fourHrsAgo = fiveHrsAgo.plusHours(1);
        String name = "My awesome Historics";
        //prepare our query
        FutureData<PreparedHistoricsQuery> query = datasift.historics()
                .prepare(stream.hash(), fiveHrsAgo, fourHrsAgo, name);
        successful(query);
        //have to create a push subscription before starting
        subscription = createPushSubscription(null, query);
        DataSiftResult historics = datasift.historics().start(query).sync();
        successful(historics);
        this.query = query.sync();
    }

    @Test
    public void get() throws InterruptedException {
        prepareAndStart();
        HistoricsQuery historics = datasift.historics().get(query.getId()).sync();
        assertEquals(query.getId(), historics.getId());
    }

    @After
    public void stopAndDelete() throws InterruptedException {
        DataSiftResult stopped = datasift.historics().stop(query, "I have a legit reason...honest").sync();
        successful(stopped);
        DataSiftResult deleted = datasift.historics().delete(query).sync();
        successful(deleted);
    }
}
