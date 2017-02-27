package com.datasift.client.pylon;

import com.datasift.client.DataSiftApiClient;
import com.datasift.client.DataSiftConfig;
import com.datasift.client.FutureData;
import com.datasift.client.ParamBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.higgs.http.client.JSONRequest;
import io.higgs.http.client.Request;
import io.higgs.http.client.readers.PageReader;

import java.net.URI;

/**
 * This class provides access to the DataSift Analysis Task API.
 */
public class DataSiftPylonTask extends DataSiftApiClient {
    protected static final String service = "linkedin";

    public final String TASK = "pylon/linkedin/task/";

    public DataSiftPylonTask(DataSiftConfig config) {
        super(config);
    }

    public FutureData<PylonTaskResultList> get(int page, int perPage) {
        FutureData<PylonTaskResultList> future = new FutureData<>();
        ParamBuilder b = new ParamBuilder();
        if (page > 0) {
            b.put("page", page);
        }
        if (perPage > 0) {
            b.put("per_page", perPage);
        }
        URI uri = b.forURL(config.newAPIEndpointURI(TASK));
        Request request = config.http().GET(uri,
                new PageReader(newRequestCallback(future, new PylonTaskResultList(), config)));
        performRequest(future, request);
        return future;
    }

    public FutureData<PylonTaskResult> get(String id) {
        URI uri = newParams().forURL(config.newAPIEndpointURI(TASK + id));
        FutureData<PylonTaskResult> future = new FutureData<>();
        Request request = config.http().GET(uri,
                new PageReader(newRequestCallback(future, new PylonTaskResult(), config)));
        performRequest(future, request);
        return future;
    }

    public FutureData<PylonTaskResult> analyze(PylonTaskRequest query) {
        if (query == null) {
            throw new IllegalArgumentException("A valid analyze request body is required to analyze a stream");
        }
        FutureData<PylonTaskResult> future = new FutureData<PylonTaskResult>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(TASK));
        try {
            JSONRequest result = config.http()
                    .postJSON(uri, new PageReader(newRequestCallback(future, new PylonTaskResult(), config)))
                    .setData(query);
            performRequest(future, result);
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException("Valid JSON is required to analyze a stream");
        }
        return future;
    }
}
