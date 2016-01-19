package com.datasift.client.behavioural;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.higgs.http.server.params.QueryParams;
import io.higgs.http.server.resource.MediaType;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Path("/behat-v1.3/")
@Produces(MediaType.APPLICATION_JSON)
public class CucumberMockWrapper {
    private String response;
    private HashMap<String, String> matchQueryStrings;
    ObjectMapper mapper = new ObjectMapper();

    @Path("/pylon/get")
    @GET
    public Object getSingleRecording(QueryParams params) throws IOException {
        if (matchQueryStrings != null) { //all query strings requested must match for us to return the given response
            for (Map.Entry<String, String> e : matchQueryStrings.entrySet()) {
                if (!Objects.equals(params.getFirst(e.getKey()), e.getValue())) {
                    throw new WebApplicationException(e.getKey() + " doesn't match expected value of " + e.getValue());
                }
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

    public CucumberMockWrapper mathQueryStringParams(HashMap<String, String> params) {
        this.matchQueryStrings = params;
        return this;
    }
}
