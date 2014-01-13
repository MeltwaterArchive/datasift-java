package com.datasift.client.mock;

import com.datasift.client.IntegrationTestBase;
import com.datasift.client.core.Validation;
import com.datasift.client.mock.datasift.MockCoreApi;
import io.higgs.core.HiggsServer;
import io.higgs.core.ObjectFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.Assert.assertTrue;

public class TestCoreApiWithMocks extends IntegrationTestBase {
    private HiggsServer server;
    HashMap<String, String> headers = new HashMap<>();

    @Before
    public void setup() throws IOException, IllegalAccessException, Exception {
        server = MockServer.startNewServer();
        config.host("localhost");
        config.setSslEnabled(false);
        config.port(server.getConfig().port);
        super.setup();
        headers.put("server", "nginx/0.8.55");
        headers.put("x-ratelimit-limit", "10000");
        headers.put("x-ratelimit-remaining", "10000");
        headers.put("x-ratelimit-cost", "5");
        server.registerObjectFactory(new ObjectFactory(server) {
            @Override
            public Object newInstance(Class<?> aClass) {
                MockCoreApi m = new MockCoreApi();
                m.setHeaders(headers);
                return m;
            }

            @Override
            public boolean canCreateInstanceOf(Class<?> aClass) {
                return MockCoreApi.class.isAssignableFrom(aClass);
            }
        });
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
