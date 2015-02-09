package com.datasift.client.mock.datasift;

import javax.ws.rs.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/v1.1/analysis")
public class MockAnalysisApi {
    Map<String, String> headers = new HashMap<>();
    private String hash;
    private double dpu;
    private boolean truncated;
    private int interactions;
    private int uniqueAuthors;
    private int volume;
    private long start, end;
    private String status;
    private int remainingAccountCapacity;
    private int remainingIndexCapacity;
    private boolean reachedCapacity;
    private Map<String, Object> parameters;
    private List<Integer> results = new ArrayList<>();
    protected long createdAt;
    private List<String> tags = new ArrayList<>();

    @Path("validate")
    public Map<String, Object> validate() {
        Map<String, Object> map = new HashMap<>();
        setStream(map);
        return map;
    }

    @Path("compile")
    public Map<String, Object> compile() {
        Map<String, Object> map = new HashMap<>();
        setStream(map);
        return map;
    }

    private void setStream(Map<String, Object> map) {
        map.put("hash", hash);
        map.put("created_at", createdAt);
        map.put("dpu", dpu);
    }

    @Path("start")
    public Map<String, Object> start() {
        Map<String, Object> map = new HashMap<>();
        return map;
    }

    @Path("stop")
    public Map<String, Object> stop() {
        Map<String, Object> map = new HashMap<>();
        return map;
    }

    @Path("analyze")
    public Map<String, Object> analyze() {
        Map<String, Object> map = new HashMap<>();
        setAnalysisResult(map);
        return map;
    }

    private void setAnalysisResult(Map<String, Object> map) {
        map.put("interactions", interactions);
        map.put("unique_authors", uniqueAuthors);
        map.put("results", results);
    }

    @Path("get")
    public Map<String, Object> get() {
        Map<String, Object> map = new HashMap<>();
        setStreamStatus(map);
        return map;
    }

    private void setStreamStatus(Map<String, Object> map) {
        map.put("hash", hash);
        map.put("volume", volume);
        map.put("start", start);
        map.put("end", end);
        map.put("status", status);
        map.put("remaining_account_capacity", remainingAccountCapacity);
        map.put("remaining_index_capacity", remainingIndexCapacity);
        map.put("reached_capacity", reachedCapacity);
    }

    @Path("tags")
    public List<String> tags() {
        this.tags.add("tag1");
        return tags;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setDpu(double dpu) {
        this.dpu = dpu;
    }

    public void setInteractions(int interactions) {
        this.interactions = interactions;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public void setUniqueAuthors(int uniqueAuthors) {
        this.uniqueAuthors = uniqueAuthors;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public void setResults(List<Integer> results) { this.results = results; }

    public void setRemainingAccountCapacity(int remainingCapacity) {
        this.remainingAccountCapacity = remainingCapacity;
    }

    public void setRemainingIndexCapacity(int remainingCapacity) {
        this.remainingIndexCapacity = remainingCapacity;
    }

    public void setReachedCapacity(boolean reachedCapacity) {
        this.reachedCapacity = reachedCapacity;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public void setStatus(String status) { this.status = status; }

    public void setTags(List<String> tags) { this.tags = tags; }

}
