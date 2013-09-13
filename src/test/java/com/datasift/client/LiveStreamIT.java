package com.datasift.client;

import com.datasift.client.core.Stream;
import com.datasift.client.stream.DataSiftMessage;
import com.datasift.client.stream.DeletedInteraction;
import com.datasift.client.stream.ErrorListener;
import com.datasift.client.stream.Interaction;
import com.datasift.client.stream.StreamEventListener;
import com.datasift.client.stream.StreamSubscription;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class LiveStreamIT extends IntegrationTestBase {
    @Before
    public void handleEvents() {
        dataSift.liveStream().onError(new ErrorListener() {
            public void exceptionCaught(Throwable t) {
                throw new RuntimeException("It crapped out for some reason", t);
            }
        });
        dataSift.liveStream().onStreamEvent(new StreamEventListener() {
            public void onDelete(DeletedInteraction di) {
                assertNotNull(di);
            }
        });
    }

    @Before
    public void testDeletes() {
        dataSift.liveStream().onStreamEvent(new StreamEventListener() {
            public void onDelete(DeletedInteraction di) {
                assertNotNull(di);
            }
        });
    }

    @Test(timeout = Settings.TIMEOUT)
    public void testWarningLogMessageReceived() {
        dataSift.liveStream().subscribe(new StreamSubscription(Stream.fromString("invalid-hash")) {
            public void onDataSiftLogMessage(DataSiftMessage dm) {
                assertNotNull(dm);
                if (!dm.isWarning()) {
                    fail(dm.toString());
                }
                doNotify();
            }

            public void onMessage(Interaction i) {
                fail("Shouldn't receive messages with an invalid hash");
            }
        });
        doWait();
    }

    @Test(timeout = Settings.TIMEOUT)
    public void testLiveStream() {
        dataSift.liveStream().subscribe(new StreamSubscription(Stream.fromString(settings.getSampleStreamHash())) {
            private int count = 0;

            public void onDataSiftLogMessage(DataSiftMessage dm) {
                assertNotNull(dm);
                if (dm.isError()) {
                    fail(dm.getMessage());
                }
            }

            public void onMessage(Interaction i) {
                assertNotNull(i);
                assertNotNull(i.get("interaction.id"));
                //all interactions are guaranteed to have an ID
                assertNotEquals("", i.get("interaction.id"));
                //after we've received N amount of interactions then consider the test passed
                if (++count >= settings.getLiveStreamCount()) {
                    doNotify();
                }
            }
        });
        doWait();
    }
}
