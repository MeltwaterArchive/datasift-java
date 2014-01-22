package com.datasift.client.mock;

import com.datasift.client.DataSiftResult;
import com.datasift.client.IntegrationTestBase;
import com.datasift.client.core.Stream;
import com.datasift.client.mock.datasift.MockPullApi;
import com.datasift.client.mock.datasift.MockPushApi;
import com.datasift.client.push.PushConnectors;
import com.datasift.client.push.PushLogMessages;
import com.datasift.client.push.PushSubscription;
import com.datasift.client.push.PushValidation;
import com.datasift.client.push.connectors.S3;
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

/**
 * Created by agnieszka on 15/01/2014.
 */
public class TestPushAPIWithMocks extends IntegrationTestBase {

    private HiggsServer server;
    private Map<String, String> headers = new HashMap<>();
    private MockPushApi m = new MockPushApi();
    private String id = new BigInteger(130, new Random()).toString(32);
    private String name = "test name";
    private int page = new Random().nextInt();
    private int size = new Random().nextInt();
    private String cursor = "";
    private Stream stream = Stream.fromString(new BigInteger(130, new Random()).toString(32));
    private String output_type = "s3";
    private long created_at = DateTime.now().getMillis();
    private String user_id = "" + new Random().nextInt();
    private String hash = new BigInteger(130, new Random()).toString(32);
    private long last_request = new Random().nextLong();
    private long last_success = new Random().nextLong();
    private long remaining_bytes = new Random().nextLong();
    private boolean lost_data = false;
    private long start = DateTime.now().getMillis();
    private long end = DateTime.now().getMillis();
    private boolean success = true;
    private int count = new Random().nextInt();

    private String s3Key = "some-random-string", s3Secret = "another-random-string", s3Buck = "s3bucket", s3Dir = "s3Dir", s3Acl = "s3acl", s3Prefix = "prefix";
    private int s3Frequency = 1234567, s3MaxSize = 3423535;
    private S3 s3;
    private String hash_type = "stream";
    private MockPullApi pull = new MockPullApi();

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
                return MockPushApi.class.isAssignableFrom(aClass);
            }
        });
        server.registerObjectFactory(new ObjectFactory(server) {
            public Object newInstance(Class<?> aClass) {
                return pull;
            }

            public boolean canCreateInstanceOf(Class<?> aClass) {
                return MockPullApi.class.isAssignableFrom(aClass);
            }
        });

        s3 = PushConnectors
                .s3()
                .accessKey(s3Key)
                .secretKey(s3Secret)
                .bucket(s3Buck)
                .directory(s3Dir)
                .acl(s3Acl)
                .deliveryFrequency(s3Frequency)
                .maxSize(s3MaxSize)
                .filePrefix(s3Prefix);
        m.setS3Params(s3.parameters().verifyAndGet());
        //
        // m.setConnector(s3);
        m.setCount(count);
        m.setCreated_at(created_at);
        m.setCursor(cursor);
        m.setEnd(end);
        m.setHash(hash);
        m.setCount(count);
        m.setId(id);
        m.setLast_request(last_request);
        m.setLast_success(last_success);
        m.setLost_data(lost_data);
        m.setName(name);
        // m.setPage(page);
        m.setRemaining_bytes(remaining_bytes);
        m.setLost_data(lost_data);
        // m.setSize(size);
        m.setStart(start);
        m.setStream(stream);
        m.setSuccess(success);
        // m.setType(output_type);
        m.setOutput_type(output_type);
        m.setUser_id(user_id);
        m.setHash_type(hash_type);
    }

    @Test
    public void testIfUserCanValidateSubscription() {
        PushValidation validation = datasift.push().validate(s3).sync();
        assertTrue(validation.isSuccessful());
    }

    @Test
    public void testIfUserCanCreateSubscription() {
        PushSubscription create = datasift.push().create(s3, stream, name).sync();
        assertTrue(create.isSuccessful());

        assertEquals(create.getId(), id);
        assertEquals(create.getOutputType().value(), output_type);
        assertEquals(create.name(), name);
        assertEquals(create.getCreatedAt(), created_at);
        assertEquals(create.getUserId(), user_id);
        assertEquals(create.hash(), hash);
        assertEquals(create.getHashType(), hash_type);
        assertEquals(create.getLastRequest(), last_request);
        assertEquals(create.getLastSuccess(), last_success);
        assertEquals(create.getRemainingBytes(), remaining_bytes);
        assertEquals(create.isLostData(), lost_data);
        assertEquals(create.getStart(), start);
        assertEquals(create.getEnd(), end);
    }

    @Test
    public void testIfUserCanPauseSubscriprion() {
        PushSubscription pause = datasift.push().pause(id).sync();
        assertTrue(pause.isSuccessful());

        assertEquals(pause.getId(), id);
        assertEquals(pause.getOutputType().value(), output_type);
        assertEquals(pause.name(), name);
        assertEquals(pause.getCreatedAt(), created_at);
        assertEquals(pause.getUserId(), user_id);
        assertEquals(pause.hash(), hash);
        assertEquals(pause.getHashType(), hash_type);
        assertEquals(pause.getLastRequest(), last_request);
        assertEquals(pause.getLastSuccess(), last_success);
        assertEquals(pause.getRemainingBytes(), remaining_bytes);
        assertEquals(pause.isLostData(), lost_data);
        assertEquals(pause.getStart(), start);
        assertEquals(pause.getEnd(), end);
    }

    @Test
    public void testIfUserCanResumeSubscription() {
        PushSubscription resume = datasift.push().resume(id).sync();
        assertTrue(resume.isSuccessful());
        assertEquals(resume.getId(), id);
        assertEquals(resume.getOutputType().value(), output_type);
        assertEquals(resume.name(), name);
        assertEquals(resume.getCreatedAt(), created_at);
        assertEquals(resume.getUserId(), user_id);
        assertEquals(resume.hash(), hash);
        assertEquals(resume.getHashType(), hash_type);
        assertEquals(resume.getLastRequest(), last_request);
        assertEquals(resume.getLastSuccess(), last_success);
        assertEquals(resume.getRemainingBytes(), remaining_bytes);
        assertEquals(resume.isLostData(), lost_data);
        assertEquals(resume.getStart(), start);
        assertEquals(resume.getEnd(), end);
    }

    @Test
    public void testIfUserCanUpdateSubscription() {
        PushSubscription update = datasift.push().update(id, s3).sync();
        assertTrue(update.isSuccessful());
        assertEquals(update.getId(), id);
        assertEquals(update.getOutputType().value(), output_type);
        assertEquals(update.name(), name);
        assertEquals(update.getCreatedAt(), created_at);
        assertEquals(update.getUserId(), user_id);
        assertEquals(update.hash(), hash);
        assertEquals(update.getHashType(), hash_type);
        assertEquals(update.getLastRequest(), last_request);
        assertEquals(update.getLastSuccess(), last_success);
        assertEquals(update.getRemainingBytes(), remaining_bytes);
        assertEquals(update.isLostData(), lost_data);
        assertEquals(update.getStart(), start);
        assertEquals(update.getEnd(), end);
    }

    @Test
    public void testIfUserCanStopSubscription() {
        PushSubscription stop = datasift.push().stop(id).sync();
        assertTrue(stop.isSuccessful());
        assertEquals(stop.getId(), id);
        assertEquals(stop.getOutputType().value(), output_type);
        assertEquals(stop.name(), name);
        assertEquals(stop.getCreatedAt(), created_at);
        assertEquals(stop.getUserId(), user_id);
        assertEquals(stop.hash(), hash);
        assertEquals(stop.getHashType(), hash_type);
        assertEquals(stop.getLastRequest(), last_request);
        assertEquals(stop.getLastSuccess(), last_success);
        assertEquals(stop.getRemainingBytes(), remaining_bytes);
        assertEquals(stop.isLostData(), lost_data);
        assertEquals(stop.getStart(), start);
        assertEquals(stop.getEnd(), end);
    }

    @Test
    public void testIfUserCanDeleteSubscription() {
        DataSiftResult delete = datasift.push().delete(id).sync();
        assertTrue(delete.isSuccessful());
    }

    @Test
    public void testIfUserCanRetrieveLogDetails() {
        PushLogMessages log_details = datasift.push().log(id, page).sync();
        assertTrue(log_details.isSuccessful());

        assertEquals(log_details.isSuccess(), success);
        assertEquals(log_details.getCount(), count);
    }

    @Test
    public void testIfUserCanGetSubscriptionDetails() {
        PushSubscription get_details = datasift.push().get(id).sync();
        assertTrue(get_details.isSuccessful());
        assertEquals(get_details.getId(), id);
        assertEquals(get_details.getOutputType().value(), output_type);
        assertEquals(get_details.name(), name);
        assertEquals(get_details.getCreatedAt(), created_at);
        assertEquals(get_details.getUserId(), user_id);
        assertEquals(get_details.hash(), hash);
        assertEquals(get_details.getHashType(), hash_type);
        assertEquals(get_details.getLastRequest(), last_request);
        assertEquals(get_details.getLastSuccess(), last_success);
        assertEquals(get_details.getRemainingBytes(), remaining_bytes);
        assertEquals(get_details.isLostData(), lost_data);
        assertEquals(get_details.getStart(), start);
        assertEquals(get_details.getEnd(), end);

    }

    /*
        @Test
        public void testIfUserCanCPullInteractions() {
            PulledInteractions pull_interactions = datasift.push().pull(id, size, cursor).sync();
            assertTrue(pull_interactions.isSuccessful());
        }
    */
    @After
    public void after() {
        server.stop();
    }


}
