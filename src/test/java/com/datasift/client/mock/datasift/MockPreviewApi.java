package com.datasift.client.mock.datasift;

import io.higgs.core.method;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by agnieszka on 17/01/2014.
 */

@method("/v1.1/sources")
public class MockPreviewApi {
    Map<String, String> headers = new HashMap<>();

    @method("create")
    public Map<String, Object> create() {
        Map<String, Object> map = new HashMap<>();

        return map;
    }

    @method("get")
    public Map<String, Object> get() {
        Map<String, Object> map = new HashMap<>();

        return map;
    }


    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

}
