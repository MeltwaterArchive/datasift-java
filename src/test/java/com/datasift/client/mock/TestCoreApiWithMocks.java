package com.datasift.client.mock;

import com.datasift.client.IntegrationTestBase;
import com.datasift.client.core.Validation;
import io.higgs.core.HiggsServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class TestCoreApiWithMocks extends IntegrationTestBase {
    private HiggsServer server;

    @Before
    public void setup() throws IOException, IllegalAccessException, Exception {
        server = MockServer.startNewServer();
        config.host("localhost");
        config.setSslEnabled(false);
        config.port(server.getConfig().port);
        super.setup();
    }

    @Test
    public void testIfUserCanValidateCSDL() {
        String str = "interaction.content contains \"apple\"";
        Validation validation = datasift.validate(str).sync();
        assertTrue(validation.isSuccessful());
        System.out.println(validation);
    }

    @After
    public void after() {
        server.stop();
    }
}
