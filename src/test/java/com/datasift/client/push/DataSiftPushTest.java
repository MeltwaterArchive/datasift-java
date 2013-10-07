package com.datasift.client.push;

import com.datasift.client.DataSiftResult;
import com.datasift.client.TestUtil;
import com.datasift.client.core.Stream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class DataSiftPushTest extends TestUtil {
    protected PushSubscription subscription;

    public DataSiftPushTest() throws Exception {
        super.setup(); //once
    }

    @Before
    public void setup() throws Exception {
        Stream stream = createStream();
        subscription = createPushSubscription(stream, null);
    }

    @After
    public void testDelete() throws InterruptedException {
        PushSubscription s = datasift.push().stop(subscription.getId()).sync();
        successful(s);
        //when invoked it may take a few moments to get to finished state depending on the stream so finished or
        // finishing all are valid
        assertTrue(s.status().isFinished() || s.status().isFinishingPaused() || s.status().isFinishing());
        DataSiftResult s2 = datasift.push().delete(subscription.getId()).sync();
        successful(s2);
    }

    @Test
    public void testPause() throws Exception {
        PushSubscription pausedSubscription = datasift.push().pause(subscription.getId()).sync();
        assertTrue(pausedSubscription.status().isPaused());
    }

    @Test
    public void testResume() throws Exception {
        PushSubscription pausedSubscription = datasift.push().pause(subscription.getId()).sync();
        assertTrue(pausedSubscription.status().isPaused());
        PushSubscription activeSubscription = datasift.push().resume(subscription.getId()).sync();
        assertTrue(activeSubscription.status().isActive());
    }

    //simple argument assertions
    @Test(expected = IllegalArgumentException.class)
    public void testPauseInvalid() throws Exception {
        datasift.push().pause(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResumeInvalid() throws Exception {
        datasift.push().resume(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStopInvalid() throws Exception {
        datasift.push().stop(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteInvalidArg() throws Exception {
        datasift.push().delete(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWithConnector() throws Exception {
        datasift.push().update(null, PushConnectors.bigQuery());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWithId() throws Exception {
        datasift.push().update("andom-id", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPull() throws Exception {
        datasift.push().pull(null, 10, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithId() throws Exception {
        datasift.push().get(null);
    }
}
