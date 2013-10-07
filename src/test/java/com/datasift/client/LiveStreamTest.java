package com.datasift.client;

import com.datasift.client.core.Stream;
import com.datasift.client.stream.DataSiftMessage;
import com.datasift.client.stream.DeletedInteraction;
import com.datasift.client.stream.Interaction;
import com.datasift.client.stream.StreamEventListener;
import com.datasift.client.stream.StreamSubscription;
import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class LiveStreamTest extends IntegrationTestBase {
    @Test(expected = IllegalStateException.class)
    public void requireErrorListener() {
        datasift.liveStream().onStreamEvent(new StreamEventListener() {
            public void onDelete(DeletedInteraction di) {
                fail("Error listener is required");
            }
        });
        datasift.liveStream().subscribe(new StreamSubscription(Stream.fromString("doesn't-matter")) {
            public void onDataSiftLogMessage(DataSiftMessage dm) {
                fail("Error listener is required");
            }

            public void onMessage(Interaction i) {
                fail("Error listener is required");
            }
        });
    }

    @Test(expected = IllegalStateException.class)
    public void requireStremEventListener() {
        datasift.liveStream().onStreamEvent(new StreamEventListener() {
            public void onDelete(DeletedInteraction di) {
                fail("Stream event listener is required");
            }
        });
        datasift.liveStream().subscribe(new StreamSubscription(Stream.fromString("doesn't-matter")) {
            public void onDataSiftLogMessage(DataSiftMessage dm) {
                fail("Stream event listener is required");
            }

            public void onMessage(Interaction i) {
                fail("Stream event listener is required");
            }
        });
    }

    public void testDeletes() {
        datasift.liveStream().subscribe(new StreamSubscription(Stream.fromString("doesn't-matter")) {
            @Override
            public void onDataSiftLogMessage(DataSiftMessage dm) {
                System.out.println("Shouldn't happen");
            }

            @Override
            public void onMessage(Interaction i) {
                System.out.println("Shouldn't happen");
            }
        });
    }
}
