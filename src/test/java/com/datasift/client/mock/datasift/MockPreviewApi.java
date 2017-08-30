package com.datasift.client.mock.datasift;

import com.datasift.client.core.Stream;
import org.joda.time.DateTime;

import javax.ws.rs.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by agnieszka on 17/01/2014.
 */

@Path("v1.4/preview")
public class MockPreviewApi {
    Map<String, String> headers = new HashMap<>();
    private DateTime createdAt;
    private String id;
    private DateTime now;
    private String[] params;
    private Stream stream;
    private String name;
    private int progress;
    private String status;
    private String feeds;
    private int sample;
    private long start;
    private long end;
    private String user;
    private String parameters;
    private String hash;
    private Map<String, Object> data = new HashMap<>();
    private String target;
    private String analysis;
    private Map<String, Long> output = new HashMap<>();
    private int threshold;

    @Path("create")
    public Map<String, Object> create() {
        Map<String, Object> map = new HashMap<>();
        map.put("created_at", createdAt);
        map.put("id", id);
        return map;
    }

    @Path("get")
    public Map<String, Object> get() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("progress", progress);
        map.put("status", status);
        map.put("feeds", feeds);
        map.put("sample", sample);
        map.put("start", start);
        map.put("end", end);
        map.put("created_at", createdAt);
        map.put("user", user);
        map.put("parameters", parameters);
        map.put("hash", hash);

        data.put("target", target);
        data.put("analysis", analysis);
        data.put("output", output);
        data.put("threshold", threshold);
        List list = new ArrayList();
        list.add(data);
        map.put("data", list);

        return map;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setNow(DateTime now) {
        this.now = now;
    }

    public void setParams(String[] params) {
        this.params = params;
    }

    public void setStream(Stream stream) {
        this.stream = stream;
    }

    public void setCreatedAt(DateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setFeeds(String feeds) {
        this.feeds = feeds;
    }

    public void setSample(int sample) {
        this.sample = sample;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public void setOutput(Map<String, Long> output) {
        this.output = output;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }
}
