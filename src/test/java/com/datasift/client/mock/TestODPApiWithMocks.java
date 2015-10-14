package com.datasift.client.mock;

import com.datasift.client.DataSiftResult;
import com.datasift.client.IntegrationTestBase;
import com.datasift.client.mock.datasift.MockAnalysisApi;
import com.datasift.client.mock.datasift.MockODPApi;
import com.datasift.client.odp.ODPBatchResponse;
import com.datasift.client.pylon.PylonStream;
import com.datasift.client.pylon.PylonStreamStatus;
import com.datasift.client.pylon.PylonTags;
import com.datasift.client.pylon.PylonValidation;
import io.higgs.core.HiggsServer;
import io.higgs.core.ObjectFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestODPApiWithMocks extends IntegrationTestBase {
    private HiggsServer server;
    private Map<String, String> headers = new HashMap<>();
    private MockODPApi m = new MockODPApi();
    private String testData = "{\"id\":\"first_interaction\"}\n{\"id\":\"second_interaction\"}";

    @Before
    public void setup() throws IOException, IllegalAccessException, Exception {
        server = MockServer.startNewServer();
        config.ingestionHost("localhost");
        config.setSslEnabled(false);
        config.port(server.getConfig().port);
        super.setup();
        headers.put("server", "nginx/0.8.55");
        headers.put("X-Ingestion-Request-RateLimit-Limit", "10000");
        headers.put("X-Ingestion-Request-RateLimit-Remaining", "9999");
        headers.put("X-Ingestion-Request-RateLimit-Reset", "57");
        headers.put("X-Ingestion-Request-RateLimit-Reset-Ttl", "1442931984");
        headers.put("X-Ingestion-Data-RateLimit-Limit", "10000000");
        headers.put("X-Ingestion-Data-RateLimit-Remaining", "10000000");
        headers.put("X-Ingestion-Data-RateLimit-Reset", "0");
        headers.put("X-Ingestion-Data-RateLimit-Reset-Ttl", "1442931927");
        server.registerObjectFactory(new ObjectFactory(server) {
            public Object newInstance(Class<?> aClass) {
                m.setHeaders(headers);
                return m;
            }

            public boolean canCreateInstanceOf(Class<?> aClass) {
                return MockAnalysisApi.class.isAssignableFrom(aClass);
            }
        });

        m.setAccepted(12345);
        m.setTotalMessageBytes(67890);
    }

    @Test
    public void testIfUserCanBatchUpload() {
        ODPBatchResponse batchResult = datasift.odp().batch("testsource", testData).sync();
        assertTrue(batchResult.isSuccessful());

        assertEquals(batchResult.getInteractionsProcessed(), 12345);
        assertEquals(batchResult.getBytesProcessed(), 67890);
    }

    @After
    public void after() {
        server.stop();
    }
}
