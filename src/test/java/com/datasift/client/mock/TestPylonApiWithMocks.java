package com.datasift.client.mock;

import com.datasift.client.DataSiftResult;
import com.datasift.client.IntegrationTestBase;
import com.datasift.client.pylon.PylonStream;
import com.datasift.client.pylon.PylonStreamStatus;
import com.datasift.client.pylon.PylonTags;
import com.datasift.client.pylon.PylonValidation;
import com.datasift.client.mock.datasift.MockPylonApi;
import io.higgs.core.HiggsServer;
import io.higgs.core.ObjectFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestPylonApiWithMocks extends IntegrationTestBase {
    private HiggsServer server;
    private Map<String, String> headers = new HashMap<>();
    private MockPylonApi m = new MockPylonApi();
    private String hash = new BigInteger(130, new Random()).toString(32);
    private Map<String, Object> parameters = new HashMap<>();
    private String status = new BigInteger(130, new Random()).toString(32);
    private String message;
    private long event_time;
    private boolean success;
    protected String csdl = "";
    private double dpu = new Random().nextDouble();
    private int interactions;
    private int uniqueAuthors;
    private int volume;
    private long start, end;
    private int remainingIndexCapacity;
    private int remainingAccountCapacity;
    private boolean reachedCapacity;
    private List<Integer> results = new ArrayList<>();
    protected long createdAt;
    private ArrayList<String> tags = new ArrayList<String>();

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
                return MockPylonApi.class.isAssignableFrom(aClass);
            }
        });

        SecureRandom random = new SecureRandom();
        csdl = "(fb.content any \"coffee\" OR fb.hashtags in \"tea\") AND fb.language in \"en\"";
        hash = new BigInteger(130, random).toString(32);
        dpu = Float.valueOf(String.valueOf(Math.random()));
        tags.add("tag1");

        m.setHash(hash);
        m.setStatus(status);
        m.setDpu(dpu);
        m.setEnd(end);
        m.setInteractions(interactions);
        m.setReachedCapacity(reachedCapacity);
        m.setRemainingIndexCapacity(remainingIndexCapacity);
        m.setRemainingAccountCapacity(remainingAccountCapacity);
        m.setStart(start);
        m.setUniqueAuthors(uniqueAuthors);
        m.setVolume(volume);
        m.setCreatedAt(createdAt);
        m.setParameters(parameters);
        m.setStatus(status);
        m.setResults(results);
        m.setTags(tags);
    }

    @Test
    public void testIfUserCanGetStreamStatus() {
        PylonStreamStatus statusResult = datasift.pylon().get(hash).sync();
        assertTrue(statusResult.isSuccessful());

        assertEquals(statusResult.getHash(), hash);
        assertEquals(statusResult.getStart(), start);
        assertEquals(statusResult.getEnd(), end);
        assertEquals(statusResult.getVolume(), volume);
        assertEquals(statusResult.getReachedCapacity(), reachedCapacity);
        assertEquals(statusResult.getRemainingAccountCapacity(), remainingAccountCapacity);
        assertEquals(statusResult.getRemainingIndexCapacity(), remainingIndexCapacity);
        assertEquals(statusResult.getStart(), start);
        assertEquals(statusResult.getStatus(), status);
    }

    @Test
    public void testIfUserCanValidateCSDL() {
        PylonValidation validationResult = datasift.pylon().validate(csdl).sync();
        assertTrue(validationResult.isSuccessful());

        assertEquals(validationResult.getDpu(), dpu, 0.1);
        assertEquals(validationResult.getCreatedAt(), createdAt);
    }

    @Test
    public void testIfUserCanCompileCSDL() {
        PylonStream compilationResult = datasift.pylon().compile(csdl).sync();
        assertTrue(compilationResult.isSuccessful());

        assertEquals(compilationResult.getDpu(), dpu, 0.1);
        assertEquals(compilationResult.getCreatedAt(), createdAt);
        assertEquals(compilationResult.hash(), hash);
    }

    @Test
    public void testIfUserCanStopDataStream() {
        DataSiftResult stop = datasift.managedSource().stop(hash).sync();
        assertTrue(stop.isSuccessful());
    }

    @Test
    public void testIfUserCanStartDataStream() {
        DataSiftResult start = datasift.pylon().start(hash).sync();
        assertTrue(start.isSuccessful());
    }

    @Test
    public void testIfUserCanGetTags() {
        PylonTags tagsResult = datasift.pylon().tags(hash).sync();
        assertTrue(tagsResult.isSuccessful());
        assertEquals(tagsResult.getTags(), this.tags);
    }

    @After
    public void after() {
        server.stop();
    }
}
