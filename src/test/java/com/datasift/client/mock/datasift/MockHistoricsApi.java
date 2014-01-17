package com.datasift.client.mock.datasift;

import com.datasift.client.historics.HistoricsQuery;
import io.higgs.core.method;
import org.joda.time.DateTime;

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
    private float dpus;
    private DateTime start;
    private DateTime end;
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


}
