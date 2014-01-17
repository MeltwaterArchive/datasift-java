package com.datasift.client.mock.datasift;

import io.higgs.core.method;
import io.higgs.http.server.HttpResponse;
import io.higgs.http.server.params.FormParam;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by agnieszka on 17/01/2014.
 */
@method("/v1.1/source")
public class MockManagedSourcesApi {
    Map<String, String> headers = new HashMap<>();

    @method("create")
    public Map<String, Object> create() {
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

    @method("log")
    public Map<String, Object> log() {
        Map<String, Object> map = new HashMap<>();

        return map;
    }

    @method("get")
    public Map<String, Object> get() {
        Map<String, Object> map = new HashMap<>();

        return map;
    }

    @method("stop")
    public Map<String, Object> stop() {
        Map<String, Object> map = new HashMap<>();

        return map;
    }

    @method("start")
    public Map<String, Object> start() {
        Map<String, Object> map = new HashMap<>();

        return map;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

}
