package com.datasift.client.mock;

import com.datasift.client.IntegrationTestBase;
import com.datasift.client.core.Stream;
import com.datasift.client.core.Validation;
import com.datasift.client.mock.datasift.MockCoreApi;
import io.higgs.core.HiggsServer;
import io.higgs.core.ObjectFactory;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestCoreApiWithMocks extends IntegrationTestBase {
    private HiggsServer server;
    private HashMap<String, String> headers = new HashMap<>();
    private MockCoreApi m = new MockCoreApi();

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
            public Object newInstance(Class<?> aClass) {
                m.setHeaders(headers);
                return m;
            }

            public boolean canCreateInstanceOf(Class<?> aClass) {
                return MockCoreApi.class.isAssignableFrom(aClass);
            }
        });
    }

    @Test
    public void testIfUserCanValidateCSDL() {
        SecureRandom random = new SecureRandom();

        String str = "interaction.content contains \"apple\"";
        String hash = new BigInteger(130, random).toString(32);
        float dpu = Float.valueOf(String.valueOf(Math.random()));
        DateTime createdAt = DateTime.now();

        m.setExpectedCsdl(str);
        m.setDpu(dpu);
        m.setCreatedAt(createdAt);
        m.setCompileHash(hash);

        Validation validation = datasift.validate(str).sync();
        assertTrue(validation.isSuccessful());

        DateTime actualDate = validation.getCreatedAt();
        float actualDpu = validation.getDpu();

        assertEquals(createdAt.getMillis(), actualDate.getMillis());
        assertEquals(dpu, actualDpu, 0.00000001);
    }

    @Test
    public void testIfUserCanCompile() {
        SecureRandom random = new SecureRandom();

        String csdl = "interaction.content contains \"apple\"";
        String hash = new BigInteger(130, random).toString(32);
        float dpu = Float.valueOf(String.valueOf(Math.random()));
        DateTime createdAt = DateTime.now();

        m.setExpectedCsdl(csdl);
        m.setDpu(dpu);
        m.setCreatedAt(createdAt);
        m.setCompileHash(hash);
        m.setExpectedCsdl(csdl);

        Stream stream = datasift.compile(csdl).sync();
        assertTrue(stream.isSuccessful());

        DateTime actualDate = stream.getCreatedAt();
        float actualDpu = stream.getDpu();

        assertEquals(createdAt.getMillis(), actualDate.getMillis());
        assertEquals(dpu, actualDpu, 0.00000001);

        assertEquals(hash,stream.hash());
    }


    @After
    public void after() {
        server.stop();
    }
}
