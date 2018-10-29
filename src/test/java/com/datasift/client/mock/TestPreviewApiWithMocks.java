package com.datasift.client.mock;

import com.datasift.client.IntegrationTestBase;
import com.datasift.client.core.Stream;
import com.datasift.client.mock.datasift.MockPreviewApi;
import com.datasift.client.preview.HistoricsPreview;
import com.datasift.client.preview.HistoricsPreviewData;
import io.higgs.core.HiggsServer;
import io.higgs.core.ObjectFactory;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/*
 * Created by agnieszka on 17/01/2014.
 */
public class TestPreviewApiWithMocks extends IntegrationTestBase {
    private HiggsServer server;
    private Map<String, String> headers = new HashMap<>();
    private MockPreviewApi m = new MockPreviewApi();
    private DateTime now = DateTime.now();
    private String[] params = {new BigInteger(130, new Random()).toString(32)};
    private Stream stream = Stream.fromString(new BigInteger(130, new Random()).toString(32));
    private DateTime createdAt = DateTime.now();
    String id = new BigInteger(130, new Random()).toString(32);

    private String name = new BigInteger(130, new Random()).toString(32);
    private int progress = new Random().nextInt();
    private String status = new BigInteger(130, new Random()).toString(32);
    private String feeds = new BigInteger(130, new Random()).toString(32);
    private int sample = new Random().nextInt();
    private long start = new Random().nextLong();
    private long end = new Random().nextLong();
    private String user = new BigInteger(130, new Random()).toString(32);
    private String parameters = new BigInteger(130, new Random()).toString(32);
    private String hash = new BigInteger(130, new Random()).toString(32);

    private String target = new BigInteger(130, new Random()).toString(32);
    private String analysis = new BigInteger(130, new Random()).toString(32);
    private Map<String, Long> output = new HashMap<>();
    private int threshold = new Random().nextInt();

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

        m.setId(id);
        m.setCreatedAt(createdAt);
        m.setName(name);
        m.setProgress(progress);
        m.setStatus(status);
        m.setFeeds(feeds);
        m.setSample(sample);
        m.setStart(start);
        m.setEnd(end);
        m.setUser(user);
        m.setParameters(parameters);
        m.setHash(hash);
        m.setTarget(target);
        m.setAnalysis(analysis);
        m.setOutput(output);
        m.setThreshold(threshold);
    }

    @Test
    public void testIfUserCanCreateHistoricsPreview() {
        HistoricsPreview create = datasift.preview().create(now, stream, params).sync();
        assertTrue(create.isSuccessful());

        assertEquals(create.id(), id);
//        assertEquals(create.getCreatedAt().getMillis(), createdAt.getMillis());
    }

    @Test
    public void testIfUserCanGetHistoricsPreview() {
        HistoricsPreviewData get = datasift.preview().get(id).sync();
        assertTrue(get.isSuccessful());

        assertEquals(get.getId(), id);
        assertEquals(get.getName(), name);
        assertEquals(get.getProgress(), progress);
        assertEquals(get.getStatus(), status);
        assertEquals(get.getFeeds(), feeds);
        assertEquals(get.getSample(), sample);
        assertEquals(get.getStart(), start);
        assertEquals(get.getEnd(), end);
        assertEquals(get.getCreated_at(), createdAt.getMillis());
        assertEquals(get.getUser(), user);
        assertEquals(get.getParameters(), parameters);
        assertEquals(get.getHash(), hash);

        for (HistoricsPreviewData.DataEntry data : get.getData()) {
            assertEquals(data.getAnalysis(), analysis);
            assertEquals(data.getOutput(), output);
            assertEquals(data.getTarget(), target);
            assertEquals(data.getThreshold(), threshold);
        }
    }

    @After
    public void after() {
        MockServer.stop();
    }
}
