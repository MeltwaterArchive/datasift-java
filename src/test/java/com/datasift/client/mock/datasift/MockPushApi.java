package com.datasift.client.mock.datasift;

import com.datasift.client.Response;
import com.datasift.client.core.Stream;
import com.datasift.client.push.PushLogMessages;
import com.datasift.client.push.Status;
import com.datasift.client.push.connectors.PushConnector;
import com.datasift.client.stream.Interaction;
import io.higgs.http.server.params.FormParams;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by agnieszka on 15/01/2014.
 */

@Path("v1.3/push")
public class MockPushApi {

    Map<String, String> headers = new HashMap<>();
    protected String cursor = "";
    Stream stream;
    protected String output_type;
    protected long created_at;
    protected String user_id = "";
    protected String hash = "";
    protected String hash_type = "";
    protected PushConnector output_params;
    protected Status status;
    protected long last_request;
    protected long last_success;
    protected long remaining_bytes;
    protected boolean lost_data;
    protected long start;
    protected long end;
    protected String error;
    protected Response response;
    protected boolean success;
    protected int count;
    protected List<PushLogMessages.PushLogMessage> log_messages;
    protected List<Interaction> interactions;
    protected String id = "";
    protected String name = "";
    protected Map<String, String> s3Params;

    @Path("/validate")
    public Map<String, Object> validate(FormParams params) {
        for (Map.Entry<String, String> e : s3Params.entrySet()) {
            Object expected = params.get(e.getKey());
            assertNotNull(expected);
            assertEquals(expected, e.getValue());
        }
        Map<String, Object> map = new HashMap<>();
        map.put("success", success);
        map.put("message", "Validated successfully");

        return map;
    }

    @Path("/create")
    public Map<String, Object> create() {
        Map<String, Object> map = new HashMap<>();
        getSubscription(map);
        return map;
    }

    @Path("/pause")
    public Map<String, Object> pause() {
        Map<String, Object> map = new HashMap<>();
        getSubscription(map);
        return map;
    }

    @Path("/resume")
    public Map<String, Object> resume() {
        Map<String, Object> map = new HashMap<>();
        getSubscription(map);
        return map;
    }

    @Path("/update")
    public Map<String, Object> update() {
        Map<String, Object> map = new HashMap<>();
        getSubscription(map);
        return map;
    }

    @Path("/stop")
    public Map<String, Object> stop() {
        Map<String, Object> map = new HashMap<>();
        getSubscription(map);
        return map;
    }

    protected void getSubscription(Map<String, Object> map) {
        map.put("id", id);
        map.put("name", name);
        map.put("output_type", output_type);
        map.put("created_at", created_at);
        map.put("user_id", user_id);
        map.put("hash", hash);
        map.put("hash_type", hash_type);
        map.put("output_params", output_params);
        map.put("status", status);
        map.put("last_request", last_request);
        map.put("last_success", last_success);
        map.put("remaining_bytes", remaining_bytes);
        map.put("lost_data", lost_data);
        map.put("start", start);
        map.put("end", end);
    }

    @Path("/delete")
    public Map<String, Object> delete() {
        Map<String, Object> map = new HashMap<>();
        getSubscription(map);
        return map;
    }

    @Path("/log")
    public Map<String, Object> log() {
        Map<String, Object> map = new HashMap<>();
        map.put("success", success);
        map.put("count", count);
        map.put("log_entries", log_messages);
        return map;
    }

    @POST
    @Path("/get")
    public Map<String, Object> get() {
        Map<String, Object> map = new HashMap<>();
        getSubscription(map);
        return map;
    }

    @Path("../pull")
    public Map<String, Object> pull() {
        Map<String, Object> map = new HashMap<>();
        map.put("interactions", interactions);

        return map;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    public void setStream(Stream stream) {
        this.stream = stream;
    }

    public void setOutput_type(String output_type) {
        this.output_type = output_type;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setHash_type(String hash_type) {
        this.hash_type = hash_type;
    }

    public void setOutput_params(PushConnector output_params) {
        this.output_params = output_params;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setLast_request(long last_request) {
        this.last_request = last_request;
    }

    public void setLast_success(long last_success) {
        this.last_success = last_success;
    }

    public void setRemaining_bytes(long remaining_bytes) {
        this.remaining_bytes = remaining_bytes;
    }

    public void setLost_data(boolean lost_data) {
        this.lost_data = lost_data;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setLog_messages(List<PushLogMessages.PushLogMessage> log_messages) {
        this.log_messages = log_messages;
    }

    public void setInteractions(List<Interaction> interactions) {
        this.interactions = interactions;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setS3Params(Map<String, String> s3Params) {
        this.s3Params = s3Params;
    }
}
