package com.datasift.client.historics;

import com.datasift.client.ApiClient;
import com.datasift.client.DataSiftConfig;
import com.datasift.client.FutureData;
import io.higgs.http.client.POST;
import io.higgs.http.client.future.PageReader;

import java.net.URI;

/**
 * This class provides access to the DataSift Historics API.
 */
public class DataSiftHistorics extends ApiClient {
    public final String VALIDATE = "push/validate", CREATE = "push/create", PAUSE = "push/pause",
            RESUME = "push/resume", UPDATE = "push/update", STOP = "push/stop", DELETE = "push/delete",
            LOG = "push/log", GET = "push/get", PULL = "push/pull";

    public DataSiftHistorics(DataSiftConfig config) {
        super(config);
    }

    /**
     * Check that the subscription details are correct
     *
     * @return the results of the validation
     */
    public FutureData<PushValidation> validate(String csdl) {
        FutureData<PushValidation> future = new FutureData<PushValidation>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(VALIDATE));
        POST request = config.http().POST(uri, new PageReader(newRequestCallback(future, new PushValidation())))
                .form("csdl", csdl);
        applyConfig(request).execute();
        return future;
    }
}
