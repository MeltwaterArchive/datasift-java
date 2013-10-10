package com.datasift.client.core;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class StreamTest {
    @Test
    public void testFromString() throws Exception {
        String val = "1233dfsc334rf34f243";
        Stream stream = Stream.fromString(val);
        assertEquals("Stream.list not producing correct hash value", val, stream.hash());
    }

    @Test
    public void testIsSameAs() throws Exception {
        String val = "1233dfsc334rf34f243";
        Stream stream1 = Stream.fromString(val);
        Stream stream2 = Stream.fromString(val);
        assertEquals("Stream.list not producing correct hash value", stream1.hash(), stream2.hash());
    }
}
