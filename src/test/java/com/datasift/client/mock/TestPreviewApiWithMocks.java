package com.datasift.client.mock;

import com.datasift.client.IntegrationTestBase;
import com.datasift.client.core.Stream;
import com.datasift.client.core.Validation;
import com.datasift.client.mock.datasift.MockCoreApi;
import com.datasift.client.mock.datasift.MockPreviewApi;
import com.datasift.client.preview.HistoricsPreview;
import com.datasift.client.preview.HistoricsPreviewData;
import io.higgs.core.HiggsServer;
import io.higgs.core.ObjectFactory;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by agnieszka on 17/01/2014.
 */
public class TestPreviewApiWithMocks extends IntegrationTestBase {
    private HiggsServer server;
    private Map<String, String> headers = new HashMap<>();
    private Map<String, Object> streams = new HashMap<>();
    private MockPreviewApi m = new MockPreviewApi();
    private DateTime now = DateTime.now();
    private String[] params;
    private Stream stream;
    private HistoricsPreview id;

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
                return MockPreviewApi.class.isAssignableFrom(aClass);
            }
        });

        stream = createStream();
    }


    @Test
    public void testIfUserCanCreateHistoricsPreview() {
        HistoricsPreview create = datasift.preview().create(now, stream, params).sync();
        assertTrue(create.isSuccessful());


    }

    @Test
    public void testIfUserCanGetHistoricsPreview() {
        HistoricsPreviewData get = datasift.preview().get(id).sync();
        assertTrue(get.isSuccessful());
    }
}
