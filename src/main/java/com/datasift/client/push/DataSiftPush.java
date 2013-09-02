package com.datasift.client.push;

import com.datasift.client.ApiClient;
import com.datasift.client.DataSiftConfig;
import com.datasift.client.FutureData;
import com.datasift.client.push.connectors.PushConnector;
import io.higgs.http.client.POST;
import io.higgs.http.client.future.PageReader;

import java.net.URI;
import java.util.Map;

/**
 * @author Courtney Robinson <courtney@crlog.info>
 */
public class DataSiftPush extends ApiClient {
    public final String VALIDATE = "push/validate", CREATE = "push/create", PAUSE = "push/pause",
            RESUME = "push/resume", UPDATE = "push/update", STOP = "push/stop", DELETE = "push/delete",
            LOG = "push/log", GET = "push/get", PULL = "push/pull";

    public DataSiftPush(DataSiftConfig config) {
        super(config);
    }

    /**
     * Check that the subscription details are correct
     *
     * @return the results of the validation
     */
    public <T extends PushConnector> FutureData<PushValidation> validate(OutputType<T> outputType, T connector) {
        FutureData<PushValidation> future = new FutureData<PushValidation>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(VALIDATE));
        POST request = config.http().POST(uri, new PageReader(newRequestCallback(future, new PushValidation())))
                .form("output_type", outputType.value());
        for (Map.Entry<String, String> e : connector.parameters().verifyAndGet().entrySet()) {
            request.form(e.getKey(), e.getValue());
        }
        applyConfig(request).execute();
        return future;
    }

    public <T extends PushConnector> FutureData<PushValidation> create(OutputType<T> outputType, T connector) {
        FutureData<PushValidation> future = new FutureData<PushValidation>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(CREATE));
        POST request = config.http().POST(uri, new PageReader(newRequestCallback(future, new PushValidation())))
                .form("output_type", outputType.value());
        for (Map.Entry<String, String> e : connector.parameters().verifyAndGet().entrySet()) {
            request.form(e.getKey(), e.getValue());
        }
        applyConfig(request).execute();
        return future;
    }
}
