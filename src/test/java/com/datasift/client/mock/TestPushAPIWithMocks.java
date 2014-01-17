package com.datasift.client.mock;

import com.datasift.client.DataSiftResult;
import com.datasift.client.IntegrationTestBase;
import com.datasift.client.Response;
import com.datasift.client.core.Stream;
import com.datasift.client.mock.datasift.MockPushApi;
import com.datasift.client.push.OutputType;
import com.datasift.client.push.PulledInteractions;
import com.datasift.client.push.PushLogMessages;
import com.datasift.client.push.PushSubscription;
import com.datasift.client.push.PushValidation;
import com.datasift.client.push.Status;
import com.datasift.client.push.connectors.CouchDB;
import com.datasift.client.push.connectors.Prepared;
import com.datasift.client.push.connectors.PushConnector;
import com.datasift.client.stream.Interaction;
import io.higgs.core.HiggsServer;
import io.higgs.core.ObjectFactory;
import io.higgs.http.client.HttpRequestBuilder;
import io.higgs.http.client.Request;
import io.higgs.http.client.future.PageReader;
import io.higgs.http.client.future.Reader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 * Created by agnieszka on 15/01/2014.
 */
public class TestPushAPIWithMocks  extends IntegrationTestBase {

    private HiggsServer server;
    private Map<String, String> headers = new HashMap<>();
    private MockPushApi m = new MockPushApi();
    private PushConnector connector = null;
    private String id = "";
    private String name = "";
    private int page = new Random().nextInt();
    private int size = new Random().nextInt();
    private String cursor = "";
    Stream stream = null;
    private OutputType<PushConnector> output_type;
    private float created_at = Float.valueOf(String.valueOf(Math.random()));
    private String user_id = "";
    private String hash = "";
    private String hash_type = "";
    private PushConnector output_params;
    private OutputType type;
    private Prepared region;
    private Status status;
    private float last_request = Float.valueOf(String.valueOf(Math.random()));
    private float last_success = Float.valueOf(String.valueOf(Math.random()));
    private float remaining_bytes = Float.valueOf(String.valueOf(Math.random()));
    private boolean lost_data = false;
    private float start = Float.valueOf(String.valueOf(Math.random()));
    private float end = Float.valueOf(String.valueOf(Math.random()));
    private String error = "";
    private Response response;
    private Throwable failure_cause;
    private boolean success = true;
    private int count = new Random().nextInt();
    private List<PushLogMessages.PushLogMessage> log_messages;
    private List<Interaction> interactions;

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

        stream = createStream();
        PushSubscription subscription = createPushSubscription(stream, null);
        id = subscription.getId();

        // initialize response
        Reader r = new PageReader();
        Request req = HttpRequestBuilder.instance().GET(new URI("http://localhost/"), r);
        io.higgs.http.client.Response httpRes = new io.higgs.http.client.Response(req, r);
        String data = "{}";
        response = new Response(data, httpRes);

        m.setConnector(connector);
        m.setCount(count);
        m.setCreated_at(created_at);
        m.setCursor(cursor);
        m.setEnd(end);
        m.setError(error);
        m.setFailure_cause(failure_cause);
        m.setHash(hash);
        m.setHash_type(hash_type);
        m.setCount(count);
        m.setId(id);
        m.setInteractions(interactions);
        m.setLast_request(last_request);
        m.setLast_success(last_success);
        m.setLog_messages(log_messages);
        m.setLost_data(lost_data);
        m.setName(name);
        m.setOutput_params(output_params);
        m.setOutput_type(output_type);
        m.setPage(page);
        m.setRegion(region);
        m.setRemaining_bytes(remaining_bytes);
        m.setResponse(response);
        m.setLost_data(lost_data);
        m.setSize(size);
        m.setStart(start);
        m.setStatus(status);
        m.setStream(stream);
        m.setSuccess(success);
        m.setType(type);
        m.setUser_id(user_id);

    }


    @Test
    public void testIfUserCanValidateSubscription() {
        PushValidation validation = datasift.push().validate(connector).sync();
        assertTrue(validation.isSuccessful());
        assertEquals(validation.getError(), error);
        assertEquals(validation.getResponse(), response);
        assertEquals(validation.failureCause(), failure_cause);
    }

    @Test
    public void testIfUserCanCreateSubscription() {
        PushSubscription create = datasift.push().create(connector, stream, name).sync();
        assertTrue(create.isSuccessful());

        assertEquals(create.getId(), id);
        assertEquals(create.getOutputType(), output_type);
        assertEquals(create.name(), name);
        assertEquals(create.getCreatedAt(), created_at, 0.00000001);
        assertEquals(create.getUserId(), user_id, 0.00000001);
        assertEquals(create.hash(), hash);
        assertEquals(create.getHashType(), hash_type);
        assertEquals(create.getOutputParams(), output_params);
        assertEquals(create.getOutputParams().type(), type);
        assertEquals(create.getOutputParams().parameters(), region); // region
        assertEquals(create.status(), status);
        assertEquals(create.getLastRequest(), last_request);
        assertEquals(create.getLastSuccess(), last_success);
        assertEquals(create.getRemainingBytes(), remaining_bytes);
        assertEquals(create.isLostData(), lost_data);
        assertEquals(create.getStart(), start, 0.00000001);
        assertEquals(create.getEnd(), end, 0.00000001);
    }

    @Test
    public void testIfUserCanPauseSubscriprion() {
        PushSubscription pause = datasift.push().pause(id).sync();
        assertTrue(pause.isSuccessful());

        assertEquals(pause.getId(), id);
        assertEquals(pause.getOutputType(), output_type);
        assertEquals(pause.name(), name);
        assertEquals(pause.getCreatedAt(), created_at);
        assertEquals(pause.getUserId(), user_id);
        assertEquals(pause.hash(), hash);
        assertEquals(pause.getHashType(), hash_type);
        assertEquals(pause.getOutputParams(), output_params);
        assertEquals(pause.status(), status);
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
        assertEquals(resume.getOutputType(), output_type);
        assertEquals(resume.name(), name);
        assertEquals(resume.getCreatedAt(), created_at);
        assertEquals(resume.getUserId(), user_id);
        assertEquals(resume.hash(), hash);
        assertEquals(resume.getHashType(), hash_type);
        assertEquals(resume.getOutputParams(), output_params);
        assertEquals(resume.status(), status);
        assertEquals(resume.getLastRequest(), last_request);
        assertEquals(resume.getLastSuccess(), last_success);
        assertEquals(resume.getRemainingBytes(), remaining_bytes);
        assertEquals(resume.isLostData(), lost_data);
        assertEquals(resume.getStart(), start);
        assertEquals(resume.getEnd(), end);
    }

    @Test
    public void testIfUserCanUpdateSubscription() {
        PushSubscription update = datasift.push().update(id, connector).sync();
        assertTrue(update.isSuccessful());
        assertEquals(update.getId(), id);
        assertEquals(update.getOutputType(), output_type);
        assertEquals(update.name(), name);
        assertEquals(update.getCreatedAt(), created_at);
        assertEquals(update.getUserId(), user_id);
        assertEquals(update.hash(), hash);
        assertEquals(update.getHashType(), hash_type);
        assertEquals(update.getOutputParams(), output_params);
        assertEquals(update.status(), status);
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
        assertEquals(stop.getOutputType(), output_type);
        assertEquals(stop.name(), name);
        assertEquals(stop.getCreatedAt(), created_at);
        assertEquals(stop.getUserId(), user_id);
        assertEquals(stop.hash(), hash);
        assertEquals(stop.getHashType(), hash_type);
        assertEquals(stop.getOutputParams(), output_params);
        assertEquals(stop.status(), status);
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

        assertEquals(delete.getError(), error);
        assertEquals(delete.getResponse(), response);
        assertEquals(delete.failureCause(), failure_cause);
    }

    @Test
    public void testIfUserCanRetrieveLogDetails() {
        PushLogMessages log_details = datasift.push().log(id, page).sync();
        assertTrue(log_details.isSuccessful());

        assertEquals(log_details.isSuccess(), success);
        assertEquals(log_details.getCount(), count);
        assertEquals(log_details.getLogMessages(), log_messages);


    }

    @Test
    public void testIfUserCanGetSubscriptionDetails() {
        PushSubscription get_details = datasift.push().get(id).sync();
        assertTrue(get_details.isSuccessful());
        assertEquals(get_details.getId(), id);
        assertEquals(get_details.getOutputType(), output_type);
        assertEquals(get_details.name(), name);
        assertEquals(get_details.getCreatedAt(), created_at);
        assertEquals(get_details.getUserId(), user_id);
        assertEquals(get_details.hash(), hash);
        assertEquals(get_details.getHashType(), hash_type);
        assertEquals(get_details.getOutputParams(), output_params);
        assertEquals(get_details.status(), status);
        assertEquals(get_details.getLastRequest(), last_request);
        assertEquals(get_details.getLastSuccess(), last_success);
        assertEquals(get_details.getRemainingBytes(), remaining_bytes);
        assertEquals(get_details.isLostData(), lost_data);
        assertEquals(get_details.getStart(), start);
        assertEquals(get_details.getEnd(), end);

    }

    @Test
    public void testIfUserCanCPullInteractions () {
        PulledInteractions pull_interactions = datasift.push().pull(id, size, cursor).sync();
        assertTrue(pull_interactions.isSuccessful());

        assertEquals(pull_interactions.getInteractions(), interactions);
    }

    @After
    public void after() {
        server.stop();
    }


}
