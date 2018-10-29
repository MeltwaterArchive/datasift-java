package com.datasift.client.mock;

import com.datasift.client.DataSiftResult;
import com.datasift.client.IntegrationTestBase;
import com.datasift.client.managedsource.ManagedSource;
import com.datasift.client.managedsource.ManagedSourceLog;
import com.datasift.client.managedsource.sources.DataSource;
import com.datasift.client.managedsource.sources.FacebookPage;
import com.datasift.client.mock.datasift.MockManagedSourcesApi;
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
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/*
 * Created by agnieszka on 17/01/2014.
 */
public class TestManagedSourcesWithMocks extends IntegrationTestBase {
    private HiggsServer server;
    private Map<String, String> headers = new HashMap<>();
    private Map<String, Object> streams = new HashMap<>();
    private MockManagedSourcesApi m = new MockManagedSourcesApi();
    private String name = new BigInteger(130, new Random()).toString(32);
    private DataSource source = new FacebookPage(config);
    private String id = new BigInteger(130, new Random()).toString(32);
    private ManagedSource m_id = ManagedSource.fromString(id);
    private String source_type = new BigInteger(130, new Random()).toString(32);
    private Map<String, Object> parameters = new HashMap<>();
    private Set auth_set;
    private Map<String, Object> auth = new HashMap<>();
    private Set resource_set;
    private Map<String, Object> resource = new HashMap<>();
    private DateTime created_at = DateTime.now();
    private int count = new Random().nextInt();
    private int page = new Random().nextInt();
    private int pages = new Random().nextInt();
    private int per_page = new Random().nextInt();
    private List<ManagedSourceLog.LogMessage> entries = new ArrayList<>();
    private String identityId = new BigInteger(130, new Random()).toString(32);
    private String sourceId = new BigInteger(130, new Random()).toString(32);
    private String status = new BigInteger(130, new Random()).toString(32);
    private String message;
    private long event_time;
    private boolean success;

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
                return MockManagedSourcesApi.class.isAssignableFrom(aClass);
            }
        });

        m.setName(name);
        m.setStatus(status);
        m.setSourceId(sourceId);
        m.setIdentityId(identityId);
        m.setEntries(entries);
        m.setPer_page(per_page);
        m.setPages(pages);
        m.setPage(page);
        m.setCount(count);
        m.setCreated_at(created_at);
        m.setResource(resource);
        m.setAuth(auth);
        m.setParameters(parameters);
        m.setSourceType(source_type);
        m.setId(id);
        m.setEvent_time(event_time);
        m.setSuccess(success);
        m.setMessage(message);
    }

    @Test
    public void testIfUserCanCreateManagedSource() {
        ManagedSource create = datasift.managedSource().create(name, source).sync();
        assertTrue(create.isSuccessful());

        assertEquals(create.getName(), name);
        assertEquals(create.getSourceType(), source_type);
        assertEquals(create.getParameters(), parameters);

        Set<ManagedSource.AuthParams> auth_array = create.getAuth();
        for (ManagedSource.AuthParams p : auth_array) {
            assertEquals(p.identityId(), identityId);
            assertEquals(p.status(), status);
            assertEquals(p.sourceId(), sourceId);
        }
        Set<ManagedSource.ResourceParams> res_set = create.getResources();
        for (ManagedSource.ResourceParams r : res_set) {
            assertEquals(r.identityId(), identityId);
            assertEquals(r.sourceId(), sourceId);
            assertEquals(r.status(), status);
        }
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
        Set<ManagedSource.AuthParams> auth_array = update.getAuth();
        for (ManagedSource.AuthParams p : auth_array) {
            assertEquals(p.identityId(), identityId);
            assertEquals(p.status(), status);
            assertEquals(p.sourceId(), sourceId);
        }
        Set<ManagedSource.ResourceParams> res_set = update.getResources();
        for (ManagedSource.ResourceParams r : res_set) {
            assertEquals(r.identityId(), identityId);
            assertEquals(r.sourceId(), sourceId);
            assertEquals(r.status(), status);
        }
        assertEquals(update.getCreatedAt(), created_at);
        assertEquals(update.getId(), id);
    }

    @Test
    public void testIfUserCanLogForManagedSource() {
        ManagedSourceLog logging_source = datasift.managedSource().log(id).sync();
        assertTrue(logging_source.isSuccessful());

        assertEquals(logging_source.getCount(), count);
        assertEquals(logging_source.getPage(), page);
        assertEquals(logging_source.getPages(), pages);
        assertEquals(logging_source.getPerPage(), per_page);
        for (ManagedSourceLog.LogMessage l : logging_source.getEntries()) {
            assertEquals(l.getMessage(), message);
            assertEquals(l.getEventTime(), event_time);
            assertEquals(l.isSuccessful(), success);
            assertEquals(l.getId(), id);
        }
    }

    @Test
    public void testIfUserCanDeleteManagedSource() {
        ManagedSource delete = datasift.managedSource().update(name, source, m_id).sync();
        assertEquals(delete.getName(), name);
        assertEquals(delete.getSourceType(), source_type);
        assertEquals(delete.getParameters(), parameters);
        Set<ManagedSource.AuthParams> auth_array = delete.getAuth();
        for (ManagedSource.AuthParams p : auth_array) {
            assertEquals(p.identityId(), identityId);
            assertEquals(p.status(), status);
            assertEquals(p.sourceId(), sourceId);
        }
        Set<ManagedSource.ResourceParams> res_set = delete.getResources();
        for (ManagedSource.ResourceParams r : res_set) {
            assertEquals(r.identityId(), identityId);
            assertEquals(r.sourceId(), sourceId);
            assertEquals(r.status(), status);
        }
        assertEquals(delete.getCreatedAt(), created_at);
        assertEquals(delete.getId(), id);

        assertTrue(delete.isSuccessful());
    }

    @Test
    public void testIfUserCanGetManagedSource() {
        ManagedSource get = datasift.managedSource().get(id).sync();
        assertTrue(get.isSuccessful());

        assertEquals(get.getName(), name);
        assertEquals(get.getSourceType(), source_type);
        assertEquals(get.getParameters(), parameters);
        Set<ManagedSource.AuthParams> auth_array = get.getAuth();
        for (ManagedSource.AuthParams p : auth_array) {
            assertEquals(p.identityId(), identityId);
            assertEquals(p.status(), status);
            assertEquals(p.sourceId(), sourceId);
        }
        Set<ManagedSource.ResourceParams> res_set = get.getResources();
        for (ManagedSource.ResourceParams r : res_set) {
            assertEquals(r.identityId(), identityId);
            assertEquals(r.sourceId(), sourceId);
            assertEquals(r.status(), status);
        }
        assertEquals(get.getCreatedAt(), created_at);
        assertEquals(get.getId(), id);
    }

    @Test
    public void testIfUserCanStopDataDelivery() {
        ManagedSource stop = datasift.managedSource().stop(id).sync();
        assertTrue(stop.isSuccessful());

        assertEquals(stop.getName(), name);
        assertEquals(stop.getSourceType(), source_type);
        assertEquals(stop.getParameters(), parameters);
        Set<ManagedSource.AuthParams> auth_array = stop.getAuth();
        for (ManagedSource.AuthParams p : auth_array) {
            assertEquals(p.identityId(), identityId);
            assertEquals(p.status(), status);
            assertEquals(p.sourceId(), sourceId);
        }
        Set<ManagedSource.ResourceParams> res_set = stop.getResources();
        for (ManagedSource.ResourceParams r : res_set) {
            assertEquals(r.identityId(), identityId);
            assertEquals(r.sourceId(), sourceId);
            assertEquals(r.status(), status);
        }
        assertEquals(stop.getId(), id);
    }

    @Test
    public void testIfUserCanStartDataDelivery() {
        DataSiftResult start = datasift.managedSource().start(id).sync();
        assertTrue(start.isSuccessful());
    }

    @After
    public void after() {
        MockServer.stop();
    }
}
