package com.datasift.client.mock;

import com.datasift.client.DataSiftResult;
import com.datasift.client.IntegrationTestBase;
import com.datasift.client.core.Validation;
import com.datasift.client.managedsource.ManagedSource;
import com.datasift.client.managedsource.ManagedSourceLog;
import com.datasift.client.managedsource.sources.DataSource;
import com.datasift.client.managedsource.sources.FacebookPage;
import com.datasift.client.mock.datasift.MockCoreApi;
import com.datasift.client.mock.datasift.MockManagedSourcesApi;
import io.higgs.core.HiggsServer;
import io.higgs.core.ObjectFactory;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by agnieszka on 17/01/2014.
 */
public class TestManagedSourcesWithMocks  extends IntegrationTestBase {
    private HiggsServer server;
    private Map<String, String> headers = new HashMap<>();
    private Map<String, Object> streams = new HashMap<>();
    private MockManagedSourcesApi m = new MockManagedSourcesApi();
    private String name = "";
    private DataSource source = new FacebookPage(config);;
    private String id;
    private ManagedSource m_id;
    private String source_type = "";
    private Map<String, Object> parameters;
    private Set<ManagedSource.AuthParams> auth;
    private Set<ManagedSource.ResourceParams> resource;
    private DateTime created_at = DateTime.now();
    private int count = new Random().nextInt();
    private int page = new Random().nextInt();
    private int pages = new Random().nextInt();
    private int per_page = new Random().nextInt();
    private List<ManagedSourceLog.LogMessage> entries;

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
    public void testIfUserCanCreateManagedSource() {
        ManagedSource create = datasift.managedSource().create(name, source).sync();
        assertTrue(create.isSuccessful());

        assertEquals(create.getName(), name);
        assertEquals(create.getSourceType(), source_type);
        assertEquals(create.getParameters(), parameters);
        assertEquals(create.getAuth(), auth);
        assertEquals(create.getResources(), resource);
        assertEquals(create.getCreatedAt(), created_at);
        assertEquals(create.getId(), id);
    }

    @Test
    public void testIfUserCanUpdateManagedSource() {
        ManagedSource update = datasift.managedSource().update(name, source, m_id).sync();
        assertTrue(update.isSuccessful());

        assertEquals(update.getName(), name);
        assertEquals(update.getSourceType(), source_type);
        assertEquals(update.getParameters(), parameters);
        assertEquals(update.getAuth(), auth);
        assertEquals(update.getResources(), resource);
        assertEquals(update.getCreatedAt(), created_at);
        assertEquals(update.getId(), id);
    }

    @Test
    public void testIfUserCanDeleteManagedSource() {
        ManagedSource delete = datasift.managedSource().update(name, source, m_id).sync();
        assertTrue(delete.isSuccessful());
    }

    @Test
    public void testIfUserCanLogForManagedSource() {
        ManagedSourceLog logging_source = datasift.managedSource().log(id).sync();
        assertTrue(logging_source.isSuccessful());

        assertEquals(logging_source.getCount(), count);
        assertEquals(logging_source.getPage(), page);
        assertEquals(logging_source.getPages(), pages);
        assertEquals(logging_source.getPerPage(), per_page);
        assertEquals(logging_source.getEntries(), entries);
    }

    @Test
    public void testIfUserCanGetManagedSource () {
        ManagedSource get = datasift.managedSource().get(id).sync();
        assertTrue(get.isSuccessful());

        assertEquals(get.getName(), name);
        assertEquals(get.getSourceType(), source_type);
        assertEquals(get.getParameters(), parameters);
        assertEquals(get.getAuth(), auth);
        assertEquals(get.getResources(), resource);
        assertEquals(get.getCreatedAt(), created_at);
        assertEquals(get.getId(), id);
    }

    @Test
    public void testIfUserCanStopDataDelivery () {
        ManagedSource stop = datasift.managedSource().stop(id).sync();
        assertTrue(stop.isSuccessful());

        assertEquals(stop.getName(), name);
        assertEquals(stop.getSourceType(), source_type);
        assertEquals(stop.getParameters(), parameters);
        assertEquals(stop.getResources(), resource);
        assertEquals(stop.getId(), id);
    }

    @Test
    public void testIfUserCanStartDataDelivery () {
        DataSiftResult start = datasift.managedSource().start(id).sync();
        assertTrue(start.isSuccessful());

    }
}
