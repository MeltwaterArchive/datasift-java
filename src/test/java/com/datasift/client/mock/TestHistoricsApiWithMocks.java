package com.datasift.client.mock;

import com.datasift.client.DataSiftResult;
import com.datasift.client.IntegrationTestBase;
import com.datasift.client.historics.HistoricsQuery;
import com.datasift.client.historics.HistoricsStatus;
import com.datasift.client.historics.PreparedHistoricsQuery;
import com.datasift.client.mock.datasift.MockHistoricsApi;
import io.higgs.core.HiggsServer;
import io.higgs.core.ObjectFactory;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by agnieszka on 17/01/2014.
 */
public class TestHistoricsApiWithMocks extends IntegrationTestBase {
    private HiggsServer server;
    private Map<String, String> headers = new HashMap<>();
    private Map<String, Object> streams = new HashMap<>();
    private MockHistoricsApi m = new MockHistoricsApi();
    private String hash = new BigInteger(130, new Random()).toString(32);
    private DateTime start = DateTime.now().plusHours(1);
    private DateTime end = DateTime.now().plusDays(1);
    private String name = new BigInteger(130, new Random()).toString(32);
    private String id = new BigInteger(130, new Random()).toString(32);
    private String reason = new BigInteger(130, new Random()).toString(32);
    private double dpus = Float.valueOf(String.valueOf(Math.random()));
    private String definition_id = new BigInteger(130, new Random()).toString(32);
    private long created_at = DateTime.now().getMillis();
    private double progress = new Random().nextDouble();
    private double sample = new Random().nextDouble();
    private List<HistoricsQuery.Chunk> chunks = new ArrayList<>();
    private Map<String, PreparedHistoricsQuery.Source> sources = new HashMap<>();
    private Map<String, PreparedHistoricsQuery.Availability> availability;
    private long status = new Random().nextLong();
    private List<Integer> versions = new ArrayList<>();
    private Map<String, Integer> augmentations = new HashMap<>();
    private String source = new BigInteger(130, new Random()).toString(32);
    private List<String> feed = new ArrayList<>();
    private long estimatedCompletion = DateTime.now().getMillis();
    private String status_g = new BigInteger(130, new Random()).toString(32);
    private ArrayList source_g = new ArrayList();
    private String src_name = new BigInteger(130, new Random()).toString(32);


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
                return MockHistoricsApi.class.isAssignableFrom(aClass);
            }
        });

        m.setDpus(dpus);
        m.setId(id);
        m.setStart(start);
        m.setEnd(end);
        m.setStatus_g(status_g);
        m.setVersions(versions);
        m.setDefinition_id(definition_id);
        m.setName(name);
        m.setCreated_at(created_at);
        m.setProgress(progress);
        m.setFeed(feed);
        m.setSample(sample);
        m.setEstimatedCompletion(estimatedCompletion);
        m.setStatus(status);
        m.setAugmentations(augmentations);
        m.setSource(source_g);
        m.setSrc_name(src_name);
    }

    @Test
    public void testIfUserCanPreparePlaybackQuery() {
        PreparedHistoricsQuery prepare = datasift.historics().prepare(hash, start, end, name).sync();
        assertTrue(prepare.isSuccessful());

        assertEquals(prepare.getDpus(), dpus, 0.00000001);
        assertEquals(prepare.getId(), id);

        assertEquals(prepare.getAvailability().getEnd(), end.getMillis());
        assertEquals(prepare.getAvailability().getStart(), start.getMillis());
        assertEquals(prepare.getAvailability().getSources().get(src_name).getAugmentations(), augmentations);
        assertEquals(prepare.getAvailability().getSources().get(src_name).getStatus(), status);
        assertEquals(prepare.getAvailability().getSources().get(src_name).getVersions(), versions);
    }

    @Test
    public void testIfUserCanStartExistingPlaybackQuery() {
        DataSiftResult start_query = datasift.historics().start(id).sync();
        assertTrue(start_query.isSuccessful());
   }

    @Test
    public void testIfUserCanStopExistingPlaybackQuery() {
        DataSiftResult stop_query = datasift.historics().stop(id, reason).sync();
        assertTrue(stop_query.isSuccessful());
    }

    @Test
    public void testIfUserCanCheckIntervalStatus() {
        HistoricsStatus status = datasift.historics().status(start, end, source).sync();
        assertTrue(status.isSuccessful());

        assertEquals(status.getStart(), start.getMillis());
        assertEquals(status.getEnd(), end.getMillis());
        assertEquals(status.getSources(), sources);
    }

    @Test
    public void testIfUserCanUpdateNameOfPlaybackQuery() {
        DataSiftResult update = datasift.historics().update(id, name).sync();
        assertTrue(update.isSuccessful());
    }

    @Test
    public void testIfUserCanDeleteOnPlaybackQuery() {
        DataSiftResult delete = datasift.historics().delete(id).sync();
        assertTrue(delete.isSuccessful());
    }

    @Test
    public void testIfUserCanGetDetailsForPlaybackQuery() {
        HistoricsQuery get = datasift.historics().get(id).sync();
        assertTrue(get.isSuccessful());

        assertEquals(get.getId(), id);
        assertEquals(get.getDefinitationId(), definition_id);
        assertEquals(get.getName(), name);
        assertEquals(get.getStart(), start.getMillis());
        assertEquals(get.getEnd(), end.getMillis());
        assertEquals(get.getCreatedAt(), created_at);
        assertEquals(get.getStatus(), status_g);
        assertEquals(get.getProgress(), progress, 0.00000001);
        assertEquals(get.getFeed(), feed);
        assertEquals(get.getSources(), source_g);
        assertEquals(get.getSample(), sample, 0.00000001);
        assertEquals(get.getChunks().get(0).getEstimatedCompletion(), estimatedCompletion);
        assertEquals(get.getChunks().get(0).getEndTime(), end.getMillis());
        assertEquals(get.getChunks().get(0).getStartTime(), start.getMillis());
        assertEquals(get.getChunks().get(0).getProgress(), progress, 0.00000001);
        assertEquals(get.getChunks().get(0).getStatus(), status_g);

    }

    @After
    public void after() {
        server.stop();
    }

}
