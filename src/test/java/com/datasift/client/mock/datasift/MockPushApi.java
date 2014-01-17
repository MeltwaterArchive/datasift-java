package com.datasift.client.mock.datasift;

import com.datasift.client.Response;
import com.datasift.client.core.Stream;
import com.datasift.client.push.OutputType;
import com.datasift.client.push.PushLogMessages;
import com.datasift.client.push.Status;
import com.datasift.client.push.connectors.Prepared;
import com.datasift.client.push.connectors.PushConnector;
import com.datasift.client.stream.Interaction;
import io.higgs.core.method;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by agnieszka on 15/01/2014.
 */

@method("/v1.1/push")
public class MockPushApi {

    Map<String, String> headers = new HashMap<>();
    private String cursor = "";
    Stream stream = null;
    private OutputType<PushConnector> output_type;
    private float created_at;
    private String user_id = "";
    private String hash = "";
    private String hash_type = "";
    private PushConnector output_params;
    private OutputType type;
    private Prepared region;
    private Status status;
    private float last_request;
    private float last_success;
    private float remaining_bytes;
    private boolean lost_data;
    private float start;
    private float end;
    private String error;
    private Response response;
    private Throwable failure_cause;
    private boolean success;
    private int count;
    private List<PushLogMessages.PushLogMessage> log_messages;
    private List<Interaction> interactions;
    private PushConnector connector = null;
    private String id = "";
    private String name = "";
    private int page;
    private int size;


    @method("/validate")
    public Map<String, Object> validate() {
        Map<String, Object> map = new HashMap<>();
        map.put("error", error);
        map.put("response", response);

        return map;
    }

    @method("/create")
    public Map<String, Object> create() {
        Map<String, Object> map = new HashMap<>();
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
        return map;
    }

    @method("/pause")
    public Map<String, Object> pause() {
        Map<String, Object> map = new HashMap<>();
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
        return map;
    }

    @method("/resume")
    public Map<String, Object> resume() {
        Map<String, Object> map = new HashMap<>();
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
        return map;
    }

    @method("/update")
    public Map<String, Object> update() {
        Map<String, Object> map = new HashMap<>();
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
        return map;
    }

    @method("/stop")
    public Map<String, Object> stop() {
        Map<String, Object> map = new HashMap<>();
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
        return map;
    }

    @method("/delete")
    public Map<String, Object> delete() {
        Map<String, Object> map = new HashMap<>();
        map.put("error", error);
        map.put("response", response);

        return map;
    }

    @method("/log")
    public Map<String, Object> log() {
        Map<String, Object> map = new HashMap<>();
        map.put("success", success);
        map.put("count", count);
        map.put("log_entries", log_messages);
        return map;
    }

    @method("/get")
    public Map<String, Object> get() {
        Map<String, Object> map = new HashMap<>();
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

        return map;
    }

    @method("../pull")
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

    public void setOutput_type(OutputType<PushConnector> output_type) {
        this.output_type = output_type;
    }

    public void setCreated_at(float created_at) {
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

    public void setType(OutputType type) {
        this.type = type;
    }

    public void setRegion(Prepared region) {
        this.region = region;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setLast_request(float last_request) {
        this.last_request = last_request;
    }

    public void setLast_success(float last_success) {
        this.last_success = last_success;
    }

    public void setRemaining_bytes(float remaining_bytes) {
        this.remaining_bytes = remaining_bytes;
    }

    public void setLost_data(boolean lost_data) {
        this.lost_data = lost_data;
    }

    public void setStart(float start) {
        this.start = start;
    }

    public void setEnd(float end) {
        this.end = end;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public void setFailure_cause(Throwable failure_cause) {
        this.failure_cause = failure_cause;
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

    public void setConnector(PushConnector connector) {
        this.connector = connector;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
