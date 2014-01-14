package com.datasift.client.mock.datasift;

import io.higgs.core.method;
import io.higgs.http.server.HttpResponse;
import io.higgs.http.server.WebApplicationException;
import io.higgs.http.server.params.FormParam;
import io.higgs.http.server.params.ValidationResult;
import io.higgs.http.server.params.valid;
import io.higgs.http.server.resource.MediaType;
import io.higgs.http.server.resource.Produces;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@method("/v1.1")
public class MockCoreApi {
    HashMap<String, String> headers = new HashMap<>();
    private float dpu = -1f;
    private DateTime createdAt = DateTime.now();
    private String compileHash;
    private String expectedCsdl;

    @method("compile")
    public Map<String, Object> compile(@FormParam("csdl") String csdl, HttpResponse response) {
        Map<String, Object> data = compileOrValidate(csdl, response);
        data.put("hash", compileHash);
        return data;
    }

    @method("validate")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> validate(@FormParam("csdl") String csdl, HttpResponse response) {
        return compileOrValidate(csdl, response);
    }

    @method("usage")
    public String usage() {
        return "";
    }

    @method("dpu")
    public String dpu() {
        return "";
    }

    @method("balance")
    public String balance() {
        return "";
    }

    public void setDpu(float dpu) {
        this.dpu = dpu;
    }

    public void setCreatedAt(DateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setCompileHash(String compileHash) {
        this.compileHash = compileHash;
    }

    public void setExpectedCsdl(String expectedCsdl) {
        this.expectedCsdl = expectedCsdl;
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    private Map<String, Object> compileOrValidate(String csdl, HttpResponse response) {
        if (!expectedCsdl.equals(csdl)) {
            throw new WebApplicationException(HttpResponseStatus.BAD_REQUEST);
        }
        return hashDpuAndCreatedAt(response);
    }

    private Map<String, Object> hashDpuAndCreatedAt(HttpResponse response) {
        for (Map.Entry<String, String> v : headers.entrySet()) {
            response.headers().add(v.getKey(), v.getValue());
        }
        Map<String, Object> map = new HashMap<>();
        map.put("created_at", createdAt);
        map.put("dpu", dpu);
        return map;
    }

}
