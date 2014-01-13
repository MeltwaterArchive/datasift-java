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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@method("/v1.1")
public class MockCoreApi {
    HashMap<String, String> headers = new HashMap<>();
    @method("compile")
    public String compile() {
        return "";
    }

    @method("validate")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> validate(@valid @FormParam("csdl") String csdl,
                                        ValidationResult res,
                                        HttpResponse response) {
        if(!res.isValid()){
            throw new WebApplicationException(HttpResponseStatus.BAD_REQUEST);
        }
        for(Map.Entry<String,String> v : headers.entrySet()){
            response.headers().add(v.getKey(),v.getValue());
        }
        System.out.println("Yeahhhh!");
        Map<String,Object> map = new HashMap<>();
        map.put("created_at","2014-01-13 11:47:10");
        map.put("dpu",0.1);
        return  map;
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

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }
}
