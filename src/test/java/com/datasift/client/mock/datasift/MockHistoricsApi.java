package com.datasift.client.mock.datasift;

import com.datasift.client.historics.HistoricsQuery;
import com.datasift.client.historics.PreparedHistoricsQuery;
import io.higgs.core.method;
import org.joda.time.DateTime;
import org.junit.After;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by agnieszka on 17/01/2014.
 */

@method("/v1.1/historics")
public class MockHistoricsApi {
    Map<String, String> headers = new HashMap<>();
    private String id;
    private double dpus;
    private long start;
    private long end;
    private int status;
    private int versions;
    private int links;
    private String definition_id;
    private String name;
    private float created_at;
    private float progress;
    private float sample;
    private String sources;
    private List<HistoricsQuery.Chunk> chunks;
    private Map<String, Object> streams = new HashMap<>();
    private String hash = "";
    private String reason = "";
    private PreparedHistoricsQuery.Availability availability;


    @method("prepare")
    public Map<String, Object> prepare() {
        Map<String, Object> map = new HashMap<>();
        map.put("dpus", dpus);
        map.put("id", id);
        map.put("start", start);
        map.put("end", end);
        map.put("status", status);
        map.put("versions", versions);
        map.put("links", links);
        return map;
    }

    @method("start")
    public Map<String, Object> start() {
        Map<String, Object> map = new HashMap<>();

        return map;
    }

    @method("stop")
    public Map<String, Object> stop() {
        Map<String, Object> map = new HashMap<>();

        return map;
    }

    @method("status")
    public Map<String, Object> status() {
        Map<String, Object> map = new HashMap<>();

        return map;
    }

    @method("update")
    public Map<String, Object> update() {
        Map<String, Object> map = new HashMap<>();

        return map;
    }

    @method("delete")
    public Map<String, Object> delete() {
        Map<String, Object> map = new HashMap<>();

        return map;
    }

    @method("get")
    public Map<String, Object> get() {
        Map<String, Object> map = new HashMap<>();

        map.put("id", id);
        map.put("definition_id", definition_id);
        map.put("name", name);
        map.put("start", start);
        map.put("end", end);
        map.put("created_at", created_at);
        map.put("status", status);
        map.put("progress", progress);
        map.put("sources", sources);
        map.put("sample", sample);
        map.put("chunks", chunks);

        return map;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setDpus(double dpus) {
        this.dpus = dpus;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setVersions(int versions) {
        this.versions = versions;
    }

    public void setLinks(int links) {
        this.links = links;
    }

    public void setDefinition_id(String definition_id) {
        this.definition_id = definition_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreated_at(float created_at) {
        this.created_at = created_at;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public void setSample(float sample) {
        this.sample = sample;
    }

    public void setSources(String sources) {
        this.sources = sources;
    }

    public void setChunks(List<HistoricsQuery.Chunk> chunks) {
        this.chunks = chunks;
    }

    public void setStreams(Map<String, Object> streams) {
        this.streams = streams;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setAvailability(PreparedHistoricsQuery.Availability availability) {
        this.availability = availability;
    }
}
