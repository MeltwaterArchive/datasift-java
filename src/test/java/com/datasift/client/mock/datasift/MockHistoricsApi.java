package com.datasift.client.mock.datasift;

import com.datasift.client.historics.HistoricsQuery;
import com.datasift.client.historics.PreparedHistoricsQuery;
import io.higgs.core.method;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by agnieszka on 17/01/2014.
 */

@method("/v1.1/historics")
public class MockHistoricsApi {
    Map<String, String> headers = new HashMap<>();
    private String id;
    private double dpus;
    private DateTime start;
    private DateTime end;
    private long status;
    private List<Integer> versions = new ArrayList<>();
    private int links;
    private String definition_id;
    private String name;
    private long created_at;
    private double progress;
    private double sample;
    private Map<String, Object> streams = new HashMap<>();
    private String hash = "";
    private String reason = "";
    private Map<String, PreparedHistoricsQuery.Source> sources = new HashMap<>();
    private ArrayList source = new ArrayList();
    private Map<String, Object> availability = new HashMap<>();
    private Map<String, Integer> augmentations = new HashMap<>();
    private List<String> feed = new ArrayList<>();
    private String status_g;
    private long estimatedCompletion;
    private String src_name;


    @method("prepare")
    public Map<String, Object> prepare() {
        Map<String, Object> map = new HashMap<>();
        map.put("dpus", dpus);
        map.put("id", id);
        availability.put("start", start.getMillis());
        availability.put("end", end.getMillis());
        PreparedHistoricsQuery.Source src = new PreparedHistoricsQuery.Source();
        src.setAugmentations(augmentations);
        src.setStatus(status);
        src.setVersions(versions);
        sources.put(src_name, src);
        availability.put("sources", sources);
        map.put("availability", availability);
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
        map.put("start", start);
        map.put("end", end);
        map.put("sources", sources);

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
        map.put("status", status_g);
        map.put("progress", progress);
        map.put("feed", feed);
        map.put("sources", source);
        map.put("sample", sample);
        List<HistoricsQuery.Chunk> chunks = new ArrayList<>();
        HistoricsQuery.Chunk chunk = new HistoricsQuery.Chunk();
        chunk.setStatus(status_g);
        chunk.setProgress(progress);
        chunk.setStartTime(start.getMillis());
        chunk.setEndTime(end.getMillis());
        chunk.setEstimatedCompletion(estimatedCompletion);
        chunks.add(chunk);
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

    public void setStart(DateTime start) {
        this.start = start;
    }

    public void setEnd(DateTime end) {
        this.end = end;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public void setSample(double sample) {
        this.sample = sample;
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

    public void setAvailability(Map<String, Object> availability) {
        this.availability = availability;
    }

    public void setSource(ArrayList source) {
        this.source = source;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public void setVersions(List<Integer> versions) {
        this.versions = versions;
    }

    public void setSources(Map<String, PreparedHistoricsQuery.Source> sources) {
        this.sources = sources;
    }

    public void setAugmentations(Map<String, Integer> augmentations) {
        this.augmentations = augmentations;
    }

    public void setFeed(List<String> feed) {
        this.feed = feed;
    }

    public void setStatus_g(String status_g) {
        this.status_g = status_g;
    }

    public void setEstimatedCompletion(long estimatedCompletion) {
        this.estimatedCompletion = estimatedCompletion;
    }

    public void setSrc_name(String src_name) {
        this.src_name = src_name;
    }
}
