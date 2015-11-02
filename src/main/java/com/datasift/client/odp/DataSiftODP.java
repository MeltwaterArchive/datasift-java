package com.datasift.client.odp;

import com.datasift.client.DataSiftApiClient;
import com.datasift.client.DataSiftConfig;
import com.datasift.client.FutureData;
import io.higgs.http.client.JSONRequest;
import io.higgs.http.client.readers.PageReader;

import java.net.URI;

/**
 * This class provides access to the DataSift ODP API.
 */
public class DataSiftODP extends DataSiftApiClient {

    public DataSiftODP(DataSiftConfig config) {
        super(config);
    }

    /**
     * Send data to DataSift ODP ingestion endpoint using batch upload method.
     *
     * @param sourceId the ODP source ID to use for data ingestion
     * @param interactions String containing new-line delimited data items
     * @return a result containing details on accepted interactions, or errors if batch failed
     */
    public FutureData<ODPBatchResponse> batch(String sourceId, String interactions) {
        FutureData<ODPBatchResponse> future = new FutureData<>();
        URI uri = newParams().forURL(config.newIngestionAPIEndpointURI(sourceId));
        JSONRequest request = config.http().postJSON(
                uri, new PageReader(newRequestCallback(future, new ODPBatchResponse(), config)))
                .setData(interactions);
        performRequest(future, request);
        return future;
    }
}
