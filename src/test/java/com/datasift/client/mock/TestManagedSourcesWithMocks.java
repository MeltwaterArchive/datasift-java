package com.datasift.client.mock;

import com.datasift.client.DataSiftResult;
import com.datasift.client.IntegrationTestBase;
import com.datasift.client.core.Validation;
import com.datasift.client.managedsource.ManagedSource;
import com.datasift.client.managedsource.ManagedSourceLog;
import com.datasift.client.managedsource.sources.DataSource;
import com.datasift.client.mock.datasift.MockCoreApi;
import com.datasift.client.mock.datasift.MockManagedSourcesApi;
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
public class TestManagedSourcesWithMocks  extends IntegrationTestBase {
    private HiggsServer server;
    private Map<String, String> headers = new HashMap<>();
    private Map<String, Object> streams = new HashMap<>();
    private MockManagedSourcesApi m = new MockManagedSourcesApi();
    private String name;
    private DataSource source;
    private String id;
    private ManagedSource m_id;

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
    }

    @Test
    public void testIfUserCanUpdateManagedSource() {
        ManagedSource update = datasift.managedSource().update(name, source, m_id).sync();
        assertTrue(update.isSuccessful());
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
    }

    @Test
    public void testIfUserCanGetManagedSource () {
        ManagedSource get = datasift.managedSource().get(id).sync();
        assertTrue(get.isSuccessful());
    }

    @Test
    public void testIfUserCanStopDataDelivery () {
        ManagedSource stop = datasift.managedSource().stop(id).sync();
        assertTrue(stop.isSuccessful());
    }

    @Test
    public void testIfUserCanStartDataDelivery () {
        DataSiftResult start = datasift.managedSource().start(id).sync();
        assertTrue(start.isSuccessful());

    }
}
