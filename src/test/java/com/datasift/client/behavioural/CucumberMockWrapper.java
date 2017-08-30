package com.datasift.client.behavioural;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.higgs.http.server.HttpStatus;
import io.higgs.http.server.params.FormParams;
import io.higgs.http.server.params.QueryParams;
import io.higgs.http.server.resource.MediaType;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.util.HashMap;

@Path("/behat-v1.4/")
@Produces(MediaType.APPLICATION_JSON)
public class CucumberMockWrapper {
    private String response;
    private String statusCode;
    private HashMap<String, String> matchQueryStrings;
    ObjectMapper mapper = new ObjectMapper();

    @Path("/pylon/get")
    @GET
    public Object getRecording(QueryParams params) throws IOException {
        if ("400".equals(statusCode)) {
            throw new WebApplicationException(mapper.readTree(response).asText(), HttpStatus.BAD_REQUEST.code());
        }
        if (response != null) {
            return mapper.readTree(response);
        }
        return new HashMap();
    }

    @Path("/pylon/analyze")
    @POST
    public Object analyze(FormParams formParams) throws IOException {
        if (statusCode.equals("400")) {
            throw new WebApplicationException(mapper.readTree(response).asText(), HttpStatus.BAD_REQUEST.code());
        }
        if (response != null) {
            return mapper.readTree(response);
        }
        return new HashMap();
    }

    @Path("/pylon/sample")
    @POST
    public Object sample(FormParams formParams) throws IOException {
        if (statusCode.equals("400")) {
            throw new WebApplicationException(mapper.readTree(response).asText(), HttpStatus.BAD_REQUEST.code());
        }
        if (response != null) {
            return mapper.readTree(response);
        }
        return new HashMap();
    }

    @Path("/pylon/start")
    @PUT
    public Object start(FormParams formParams) throws IOException {
        if (statusCode.equals("400")) {
            throw new WebApplicationException(mapper.readTree(response).asText(), HttpStatus.BAD_REQUEST.code());
        }
        if (response != null) {
            return mapper.readTree(response);
        }
        return new HashMap();
    }

    @Path("/pylon/stop")
    @PUT
    public Object stop(QueryParams params) throws IOException {
        if (statusCode.equals("400")) {
            throw new WebApplicationException(mapper.readTree(response).asText(), HttpStatus.BAD_REQUEST.code());
        }
        if (response != null) {
            return mapper.readTree(response);
        }
        return new HashMap();
    }

    @Path("/pylon/tags")
    @GET
    public Object tags(QueryParams params) throws IOException {
        if (statusCode.equals("400")) {
            throw new WebApplicationException(mapper.readTree(response).asText(), HttpStatus.BAD_REQUEST.code());
        }
        if (response != null) {
            return mapper.readTree(response);
        }
        return new HashMap();
    }

    @Path("/pylon/update")
    @PUT
    public Object update(FormParams formParams) throws IOException {
        if (statusCode.equals("400")) {
            throw new WebApplicationException(mapper.readTree(response).asText(), HttpStatus.BAD_REQUEST.code());
        }
        if (response != null) {
            return mapper.readTree(response);
        }
        return new HashMap();
    }

    public CucumberMockWrapper response(String body) {
        this.response = body;
        return this;
    }

    public CucumberMockWrapper matchQueryStringParams(HashMap<String, String> params) {
        this.matchQueryStrings = params;
        return this;
    }

    public CucumberMockWrapper statusCode(String statusCode) {
        this.statusCode = statusCode;
        return this;
    }
}
