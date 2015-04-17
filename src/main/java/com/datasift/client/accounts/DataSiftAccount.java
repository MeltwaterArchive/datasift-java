package com.datasift.client.accounts;

import com.datasift.client.DataSiftApiClient;
import com.datasift.client.DataSiftConfig;
import com.datasift.client.FutureData;
import io.higgs.http.client.POST;
import io.higgs.http.client.readers.PageReader;

import java.net.URI;

/**
 * @author Courtney Robinson <courtney@crlog.info>
 */
public class DataSiftAccount extends DataSiftApiClient {
    public final String IDENTITY = "account/identity";

    public DataSiftAccount(DataSiftConfig config) {
        super(config);
    }

    public FutureData<Identity> create(String label) {
        return create(label, true, false);
    }

    public FutureData<Identity> create(String label, boolean active) {
        return create(label, active, false);
    }

    public FutureData<Identity> create(String label, boolean active, boolean master) {
        if (label == null || label.isEmpty()) {
            throw new IllegalArgumentException("A label is required");
        }
        String activeStr = active ? "active" : "disabled";
        FutureData<Identity> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(IDENTITY));
        POST request = config.http()
                .POST(uri, new PageReader(newRequestCallback(future, new Identity(), config)))
                .form("label", label).form("active", activeStr).form("master", master);
        performRequest(future, request);
        return future;
    }

}
