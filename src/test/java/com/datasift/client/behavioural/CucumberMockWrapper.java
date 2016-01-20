package com.datasift.client.behavioural;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.higgs.http.server.HttpStatus;
import io.higgs.http.server.params.QueryParams;
import io.higgs.http.server.resource.MediaType;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.util.HashMap;

@Path("/behat-v1.3/")
@Produces(MediaType.APPLICATION_JSON)
public class CucumberMockWrapper {
    private String response;
    private String statusCode;
    private HashMap<String, String> matchQueryStrings;
    ObjectMapper mapper = new ObjectMapper();

    @Path("/pylon/get")
    @GET
    public Object getRecording(QueryParams params) throws IOException {
        if (matchQueryStrings != null) { //all query strings requested must match for us to return the given response
            if (statusCode.equals("400")) {
                throw new WebApplicationException(mapper.readTree(response).asText(), HttpStatus.BAD_REQUEST.code());
            }
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
