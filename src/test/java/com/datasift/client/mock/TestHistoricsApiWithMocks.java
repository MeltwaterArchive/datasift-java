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
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private String hash;
    private DateTime start;
    private DateTime end;
    private String name;
    private String id;
    private String reason;
    private String sources;
    private PreparedHistoricsQuery.Availability availability;
    private float dpus;
    private String definition_id;
    private float created_at;
    private String status;
    private float progress;
    private float sample;
    private List<HistoricsQuery.Chunk> chunks;


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

    }

    @Test
    public void testIfUserCanPreparePlaybackQuery() {
        PreparedHistoricsQuery prepare = datasift.historics().prepare(hash, start, end, name).sync();
        assertTrue(prepare.isSuccessful());

        assertEquals(prepare.getAvailability(), availability);
        assertEquals(prepare.getDpus(), dpus, 0.00000001);
        assertEquals(prepare.getId(), id);
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
        HistoricsStatus status = datasift.historics().status(start, end, sources).sync();
        assertTrue(status.isSuccessful());

        assertEquals(status.getStart(), start);
        assertEquals(status.getEnd(), end);
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
        assertEquals(get.getStart(), start);
        assertEquals(get.getEnd(), end);
        assertEquals(get.getCreatedAt(), created_at);
        assertEquals(get.getStatus(), status);
        assertEquals(get.getProgress(), progress);
        assertEquals(get.getSources(), sources);
        assertEquals(get.getSample(), sample);
        assertEquals(get.getChunks(), chunks);
    }

}
