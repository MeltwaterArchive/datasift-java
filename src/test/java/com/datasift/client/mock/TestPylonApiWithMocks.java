package com.datasift.client.mock;

import com.datasift.client.DataSiftResult;
import com.datasift.client.FutureData;
import com.datasift.client.FutureResponse;
import com.datasift.client.IntegrationTestBase;
import com.datasift.client.pylon.PylonSample;
import com.datasift.client.pylon.PylonSampleInteraction;
import com.datasift.client.pylon.PylonSampleInteractionItem;
import com.datasift.client.pylon.PylonSampleInteractionParent;
import com.datasift.client.pylon.PylonSampleRequest;
import com.datasift.client.pylon.PylonStream;
import com.datasift.client.pylon.PylonRecording;
import com.datasift.client.pylon.PylonTags;
import com.datasift.client.pylon.PylonValidation;
import com.datasift.client.mock.datasift.MockPylonApi;
import com.datasift.client.pylon.PylonRecording.PylonRecordingId;
import io.higgs.core.HiggsServer;
import io.higgs.core.ObjectFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

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
    private String recordingId = new BigInteger(160, new Random()).toString(32);
    private String hash = new BigInteger(128, new Random()).toString(32);
    private Map<String, Object> parameters = new HashMap<>();
    private String status = "running";
    private String message;
    private long event_time;
    private boolean success;
    protected String csdl = "";
    private double dpu = new Random().nextDouble();
    private int interactions;
    private int uniqueAuthors;
    private int volume = 999998;
    private long start = 100, end = 1000;
    private int remainingIndexCapacity = 2;
    private int remainingAccountCapacity = 2;
    private boolean reachedCapacity;
    private List<Integer> results = new ArrayList<>();
    protected long createdAt;
    private ArrayList<String> tags = new ArrayList<String>();

    private int sampleRemaining = 50;
    private int sampleResetAt = 1442317895;
    private String sampleSubtype = "story";
    private String sampleMediaType = "post";
    private String sampleContent = "I love data!";
    private String sampleLanguage = "en";
    private List<Long> sampleTopicIDs = new ArrayList<Long>() { {
        add(565634324L);
    } };

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

        m.setRecordingId(recordingId);
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
        m.setSampleRemaining(sampleRemaining);
        m.setSampleResetAt(sampleResetAt);
        m.setSampleSubtype(sampleSubtype);
        m.setSampleMediaType(sampleMediaType);
        m.setSampleContent(sampleContent);
        m.setSampleLanguage(sampleLanguage);
        m.setSampleTopicIDs(sampleTopicIDs);
    }

    @Test
    public void testIfUserCanGetStreamStatus() {
        PylonRecording statusResult = datasift.pylon().get(new PylonRecordingId(recordingId)).sync();
        assertTrue(statusResult.isSuccessful());

        assertEquals(statusResult.getRecordingId().getId(), recordingId);
        assertEquals(statusResult.getHash(), hash);
        assertEquals(statusResult.getStart(), start);
        assertEquals(statusResult.getEnd(), end);
        assertEquals(statusResult.getVolume(), volume);
        assertEquals(statusResult.isReachedCapacity(), reachedCapacity);
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
        DataSiftResult stop = datasift.pylon().stop(new PylonRecordingId(recordingId)).sync();
        assertTrue(stop.isSuccessful());
    }

    @Test
    public void testIfUserCanStopDataStreamAndItReadsId() {
        PylonRecordingId rid = mock(PylonRecordingId.class);
        when(rid.getId()).thenReturn(recordingId);
        DataSiftResult stop = datasift.pylon().stop(rid).sync();
        verify(rid, times(3)).getId();
        assertTrue(stop.isSuccessful());
    }

    @Test
    public void testIfUserCanStartDataStream() {
        DataSiftResult start = datasift.pylon().start(PylonStream.fromString(hash)).sync();
        assertTrue(start.isSuccessful());
    }

    @Test
    public void testIfUserCanRestartDataStream() {
        DataSiftResult start = datasift.pylon().restart(new PylonRecordingId(recordingId)).sync();
        assertTrue(start.isSuccessful());
    }

    @Test
    public void testIfUserCanUpdateDataStream() {
        DataSiftResult update = datasift.pylon().update(new PylonRecordingId(recordingId), "newName").sync();
        assertTrue(update.isSuccessful());
    }

    @Test
    public void testIfUserCanGetTags() {
        PylonTags tagsResult = datasift.pylon().tags(new PylonRecordingId(recordingId)).sync();
        assertTrue(tagsResult.isSuccessful());
        assertEquals(tagsResult.getTags(), this.tags);
    }

    @Test
    public void testIfUserCanGetSample() {
        PylonSampleRequest sampleRequest = new PylonSampleRequest(new PylonRecordingId(recordingId));
        PylonSample sampleResult = datasift.pylon().sample(sampleRequest).sync();
        assertTrue(sampleResult.isSuccessful());
        assertEquals(sampleResult.getRateLimitResetTime(), sampleResetAt);
        assertEquals(sampleResult.getRemainingRateLimit(), sampleRemaining);

        PylonSampleInteractionItem interactionItem = sampleResult.getInteractions().get(0);
        PylonSampleInteraction interaction = interactionItem.getInteraction();
        PylonSampleInteractionParent parent = interactionItem.getInteractionParent();
        assertEquals(interaction.getContent(), sampleContent);
        assertEquals(interaction.getLanguage(), sampleLanguage);
        assertEquals(interaction.getMediaType(), sampleMediaType);
        assertEquals(interaction.getTopicIDs(), sampleTopicIDs);
        assertEquals(parent.getContent(), sampleContent);
        assertEquals(parent.getSubtype(), sampleSubtype);
    }

    @After
    public void after() {
        MockServer.stop();
    }
}
