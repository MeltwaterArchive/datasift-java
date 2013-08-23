package com.datasift.client.core;

import com.datasift.client.ApiClient;
import com.datasift.client.DataSiftConfig;
import com.datasift.client.FutureData;
import io.higgs.http.client.POST;
import io.higgs.http.client.Request;
import io.higgs.http.client.future.PageReader;

import javax.validation.constraints.NotNull;
import java.net.URI;

import static com.datasift.client.core.Usage.Period;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class DataSiftCore extends ApiClient {
    public final String VALIDATE = "validate", COMPILE = "compile", BALANCE = "balance", DPU = "dpu", USAGE = "usage";

    public DataSiftCore(DataSiftConfig config) {
        super(config);
    }

    /**
     * Validate the given CSDL string against the DataSift API
     *
     * @param csdl the CSDL to validate
     * @return the results of the validation, use {@link com.datasift.client.core.Validation#isSuccessful()} to check if
     *         validation was successful or not
     */
    public FutureData<Validation> validate(String csdl) {
        FutureData<Validation> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(VALIDATE));
        POST request = config.http().POST(uri, new PageReader(newRequestCallback(future, Validation.class)))
                .form("csdl", csdl);
        applyConfig(request).execute();
        return future;
    }

    /**
     * Compile a CSDL string to a stream hash to which you can later subscribe and receive interactions from
     *
     * @param csdl the CSDL to compile
     * @return a stream object representing the DataSift compiled CSDL, use {@link com.datasift.client.core.Stream#hash()}
     *         to get the hash for the compiled CSDL
     */
    public FutureData<Stream> compile(String csdl) {
        FutureData<Stream> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(COMPILE));
        POST request = config.http().POST(uri, new PageReader(newRequestCallback(future, Stream.class)))
                .form("csdl", csdl);
        applyConfig(request).execute();
        return future;
    }

    /**
     * @return The balance on the account being used to make API calls
     */
    public FutureData<Balance> balance() {
        FutureData<Balance> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(BALANCE));
        Request request = config.http().GET(uri, new PageReader(newRequestCallback(future, Balance.class)));
        applyConfig(request).execute();
        return future;
    }

    /**
     * @param stream the stream for which the DPU information is to be fetched
     * @return a DPU breakdown of the stream's usage
     */
    public FutureData<Dpu> dpu(@NotNull Stream stream) {
        FutureData<Dpu> future = new FutureData<>();
        URI uri = newParams().put("hash", stream.hash()).forURL(config.newAPIEndpointURI(DPU));
        Request request = config.http().GET(uri, new PageReader(newRequestCallback(future, Dpu.class)));
        applyConfig(request).execute();
        return future;
    }

    public FutureData<Usage> usage() {
        return usage(Period.DAY);
    }

    /**
     * @param timePeriod A time period during which the usage information should be broken down
     * @return a break down over the time period specified
     */
    public FutureData<Usage> usage(Period timePeriod) {
        FutureData<Usage> future = new FutureData<>();
        String period = null;
        switch (timePeriod) {
            case HOUR:
                period = "hour";
                break;
            case CURRENT:
                period = "current";
                break;
            case DAY:
            default:
                period = "day";
        }
        URI uri = newParams().put("period", period).forURL(config.newAPIEndpointURI(USAGE));
        Request request = config.http().GET(uri, new PageReader(newRequestCallback(future, Usage.class)));
        applyConfig(request).execute();
        return future;
    }
     /*
    public FutureData<BufferedStream> stream(String csdl) {
        FutureData<BufferedStream> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(VALIDATE));
        Request request = config.http().GET(uri, new PageReader(newRequestCallback(future, BufferedStream.class)))
                .form("csdl", csdl);
        applyConfig(request).execute();
        return future;
    }
    */
}
