package com.datasift.client.analysis;

import com.datasift.client.BaseDataSiftResult;
import com.datasift.client.DataSiftApiClient;
import com.datasift.client.DataSiftConfig;
import com.datasift.client.DataSiftResult;
import com.datasift.client.FutureData;
import com.datasift.client.core.Stream;
import com.datasift.client.core.Validation;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.higgs.http.client.Request;
import io.higgs.http.client.JSONRequest;
import io.higgs.http.client.readers.PageReader;

import java.net.URI;
import java.util.ArrayList;
import java.util.Map;

/**
 * This class provides access to the DataSift Analysis API.
 */
public class DataSiftAnalysis extends DataSiftApiClient {
    public final String VALIDATE = "analysis/validate", COMPILE = "analysis/compile", START = "analysis/start", STOP = "analysis/stop", GET = "analysis/get", ANALYZE = "analysis/analyze", TAGS = "analysis/tags";

    public DataSiftAnalysis(DataSiftConfig config) {
        super(config);
    }

    /**
     * Validate the given CSDL string
     *
     * @param csdl the CSDL to validate
     * @return the results of the validation, use {@link com.datasift.client.core.Validation#isSuccessful()} to check if
     * validation was successful or not
     */
    public FutureData<Validation> validate(String csdl) {
        FutureData<Validation> future = new FutureData<Validation>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(VALIDATE));
        JSONRequest request = config.http().postJSON(uri, new PageReader(newRequestCallback(future, new Validation(), config)))
                .setData(csdl);
        performRequest(future, request);
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
        JSONRequest request = config.http().postJSON(uri, new PageReader(newRequestCallback(future, new Stream(), config)))
                .setData(csdl);
        performRequest(future, request);
        return future;
    }

    /**
     * Start the stream with the given hash
     *
     * @param hash the stream hash
     * @return a result which can be checked for success or failure, A status 204 indicates success,
     * or using {@link com.datasift.client.BaseDataSiftResult#isSuccessful()}
     */
    public FutureData<DataSiftResult> start(String hash) {
        return start(hash, null);
    }

    /**
     * @param f start a stream
     * @return this
     */
    protected FutureData<DataSiftResult> start(String hash, FutureData<DataSiftResult> f) {
        if (hash == null || hash.isEmpty()) {
            throw new IllegalArgumentException("A valid hash is required to start a stream");
        }
        FutureData<DataSiftResult> future = f != null ? f : new FutureData<DataSiftResult>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(START));
        JSONRequest request = config.http()
                .putJSON(uri, new PageReader(newRequestCallback(future, new BaseDataSiftResult(), config)))
                .setData(hash);
        performRequest(future, request);
        return future;
    }

    /**
     * Stop the stream with the given hash
     *
     * @param hash the stream hash
     * @return a result which can be checked for success or failure, A status 204 indicates success,
     * or using {@link com.datasift.client.BaseDataSiftResult#isSuccessful()}
     */
    public FutureData<DataSiftResult> stop(String hash) {
        return stop(hash, null);
    }

    /**
     * @param f start a stream
     * @return this
     */
    protected FutureData<DataSiftResult> stop(String hash, FutureData<DataSiftResult> f) {
        if (hash == null || hash.isEmpty()) {
            throw new IllegalArgumentException("A valid hash is required to stop a stream");
        }
        FutureData<DataSiftResult> future = f != null ? f : new FutureData<DataSiftResult>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(STOP));
        JSONRequest request = config.http()
                .putJSON(uri, new PageReader(newRequestCallback(future, new BaseDataSiftResult(), config)))
                .setData(hash);
        performRequest(future, request);
        return future;
    }

    /**
     * @return the status of all streams that are running or have run with stored data
     */
    public FutureData<AnalysisStreamStatusList> get() {
        URI uri = newParams().forURL(config.newAPIEndpointURI(GET));
        FutureData<AnalysisStreamStatusList> future = new FutureData<>();
        Request request = config.http().GET(uri, new PageReader(newRequestCallback(future, new AnalysisStreamStatusList(), config)));
        performRequest(future, request);
        return future;
    }

    /**
     * @param hash A stream hash
     * @return the status of the requested stream
     */
    public FutureData<AnalysisStreamStatus> get(String hash) {
        URI uri = newParams().put("hash", hash).forURL(config.newAPIEndpointURI(GET));
        FutureData<AnalysisStreamStatus> future = new FutureData<>();
        Request request = config.http().GET(uri, new PageReader(newRequestCallback(future, new AnalysisStreamStatus(), config)));
        performRequest(future, request);
        return future;
    }

    /**
     * @param query analysis options for a stream
     * @return information on execution of a stream
     */
    public FutureData<AnalyzeResult> analyze(AnalyzeQuery query) {
        if (query == null) {
            throw new IllegalArgumentException("A valid analyze request body is required to analyze a stream");
        }
        FutureData<AnalyzeResult> future = new FutureData<AnalyzeResult>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(ANALYZE));
        try {
            JSONRequest result = config.http()
                    .postJSON(uri, new PageReader(newRequestCallback(future, new AnalyzeResult(), config)))
                    .setData(query);
            performRequest(future, result);
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException("Valid JSON is required to analyze a stream");
        }
        return future;
    }

    /**
     * @param hash A filter hash
     * @return vedo tags for the given filter
     */
    public FutureData<AnalysisTags> tags(String hash) {
        URI uri = newParams().put("hash", hash).forURL(config.newAPIEndpointURI(TAGS));
        FutureData<AnalysisTags> future = new FutureData<>();
        Request request = config.http().GET(uri, new PageReader(newRequestCallback(future, new AnalysisTags(), config)));
        performRequest(future, request);
        return future;
    }
}