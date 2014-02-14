package com.datasift.client.mock;

import com.datasift.client.core.Validation;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestCoreApiWithMocksWithSSL extends TestCoreApiWithMocks {

    @Before
    public void setup() throws IOException, IllegalAccessException, Exception {
        super.setup();
        config.setSslEnabled(true);
    }

    @Test
    public void testIfUserCanValidateCSDL() {
        Validation validation = datasift.validate(csdl).sync();
        assertTrue(validation.isSuccessful());

        DateTime actualDate = validation.getCreatedAt();
        float actualDpu = validation.getDpu();

        assertEquals(createdAt.getMillis(), actualDate.getMillis());
        assertEquals(dpu, actualDpu, 0.00000001);
    }

    @After
    public void after() {
        server.stop();
    }
}
