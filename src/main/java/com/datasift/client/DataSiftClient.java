package com.datasift.client;

import com.datasift.client.core.Balance;
import com.datasift.client.core.Dpu;
import com.datasift.client.core.Stream;
import com.datasift.client.core.Usage;
import com.datasift.client.core.Validation;
import com.datasift.client.historics.DataSiftHistorics;
import com.datasift.client.managedsource.DataSiftManagedSource;
import com.datasift.client.preview.DataSiftPreview;
import com.datasift.client.push.DataSiftPush;
import com.datasift.client.stream.StreamingData;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import io.higgs.http.client.HttpRequestBuilder;
import io.higgs.http.client.POST;
import io.higgs.http.client.Request;
import io.higgs.http.client.future.PageReader;

import java.net.URI;
import java.text.SimpleDateFormat;

/**
 * This class is the gateway to the DataSift client APIs. It provides an easy to use,
 * configurable interface for accessing all DataSift features
 */
public class DataSiftClient extends DataSiftApiClient {
    public static final ObjectMapper MAPPER = new ObjectMapper();
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * Where ever a numeric value is optional and it happens to be absent in a response this value will be used
     */
    public static final int DEFAULT_NUM = Integer.MIN_VALUE;
    protected DataSiftConfig config;
    protected DataSiftHistorics historics;
    protected DataSiftManagedSource source;
    protected DataSiftPreview preview;
    protected DataSiftPush push;
    protected StreamingData liveStream;
    public final String VALIDATE = "validate", COMPILE = "compile", BALANCE = "balance", DPU = "dpu", USAGE = "usage";

    public DataSiftClient() {
        this(new DataSiftConfig());
    }

    /**
     * @param config a configuration which should be used for making API requests
     */
    public DataSiftClient(DataSiftConfig config) {
        super(config);
        configureMapper();
        this.config = config;
        this.historics = new DataSiftHistorics(config);
        this.source = new DataSiftManagedSource(config);
        this.preview = new DataSiftPreview(config);
        this.push = new DataSiftPush(config);
        this.liveStream = new StreamingData(config);
    }

    protected void configureMapper() {
        MAPPER.setDateFormat(new SimpleDateFormat(DATE_FORMAT));
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        MAPPER.registerModule(new JodaModule());
    }

    /**
     * @return An object suitable for making requests to the DataSift Historics API
     */
    public DataSiftHistorics historics() {
        return historics;
    }

    /**
     * @return An object suitable for making requests to the DataSift Managed sources API
     */
    public DataSiftManagedSource managedSource() {
        return source;
    }

    /**
     * @return An object suitable for making requests to the DataSift Preview API
     */
    public DataSiftPreview preview() {
        return preview;
    }

    /**
     * @return An object suitable for making requests to the DataSift Push API
     */
    public DataSiftPush push() {
        return push;
    }

    /**
     * @return the instance of the configuration being used for all API calls made through this client
     */
    public DataSiftConfig config() {
        return config;
    }

    /**
     * Access to the DataSift streaming API
     */
    public StreamingData liveStream() {
        return liveStream;
    }

    /**
     * Validate the given CSDL string against the DataSift API
     *
     * @param csdl the CSDL to validate
     * @return the results of the validation, use {@link com.datasift.client.core.Validation#isSuccessful()} to check if
     * validation was successful or not
     */
    public FutureData<Validation> validate(String csdl) {
        FutureData<Validation> future = new FutureData<Validation>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(VALIDATE));
        POST request = config.http().POST(uri, new PageReader(newRequestCallback(future, new Validation(), config)))
                .form("csdl", csdl);
        applyConfig(request).execute();
        return future;
    }

    /**
     * Compile a CSDL string to a stream hash to which you can later subscribe and receive interactions from
     *
     * @param csdl the CSDL to compile
     * @return a stream object representing the DataSift compiled CSDL, use {@link com.datasift.client.core
     * .Stream#hash()}
     * to list the hash for the compiled CSDL
     */
    public FutureData<Stream> compile(String csdl) {
        FutureData<Stream> future = new FutureData<Stream>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(COMPILE));
        POST request = config.http().POST(uri, new PageReader(newRequestCallback(future, new Stream(), config)))
                .form("csdl", csdl);
        applyConfig(request).execute();
        return future;
    }

    /**
     * @return The balance on the account being used to make API calls
     */
    public FutureData<Balance> balance() {
        FutureData<Balance> future = new FutureData<Balance>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(BALANCE));
        Request request = config.http().GET(uri, new PageReader(newRequestCallback(future, new Balance(), config)));
        applyConfig(request).execute();
        return future;
    }

    /**
     * @param stream the stream for which the DPU information is to be fetched
     * @return a DPU breakdown of the stream's usage
     */
    public FutureData<Dpu> dpu(Stream stream) {
        return dpu(FutureData.wrap(stream));
    }

    public FutureData<Dpu> dpu(FutureData<Stream> streamFuture) {
        final FutureData<Dpu> future = new FutureData<Dpu>();
        final Dpu dpu = new Dpu();
        //
        final FutureResponse<Stream> response = new FutureResponse<Stream>() {
            public void apply(Stream stream) {
                URI uri = newParams().put("hash", stream.hash()).forURL(config.newAPIEndpointURI(DPU));
                Request request = config.http().GET(uri, new PageReader(newRequestCallback(future, dpu, config)));
                applyConfig(request).execute();
            }
        };
        unwrapFuture(streamFuture, future, dpu, response);
        return future;
    }

    public FutureData<Usage> usage() {
        return usage(Usage.Period.DAY);
    }

    /**
     * @param timePeriod A time period during which the usage information should be broken down
     * @return a break down over the time period specified
     */
    public FutureData<Usage> usage(Usage.Period timePeriod) {
        FutureData<Usage> future = new FutureData<Usage>();
        String period;
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
        Request request = config.http().GET(uri, new PageReader(newRequestCallback(future, new Usage(), config)));
        applyConfig(request).execute();
        return future;
    }

    public void shutdown() {
        HttpRequestBuilder.shutdown();
    }
}
