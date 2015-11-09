package com.datasift.client.pylon;

import com.datasift.client.BaseDataSiftResult;
import com.datasift.client.DataSiftApiClient;
import com.datasift.client.DataSiftConfig;
import com.datasift.client.DataSiftResult;
import com.datasift.client.FutureData;
import com.datasift.client.ParamBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.higgs.http.client.Request;
import io.higgs.http.client.JSONRequest;
import io.higgs.http.client.readers.PageReader;

import java.net.URI;

/**
 * This class provides access to the DataSift Analysis API.
 */
public class DataSiftPylon extends DataSiftApiClient {
    public final String VALIDATE = "pylon/validate", COMPILE = "pylon/compile", START = "pylon/start",
            STOP = "pylon/stop", GET = "pylon/get", ANALYZE = "pylon/analyze", TAGS = "pylon/tags", SAMPLE = "pylon/sample";

    public DataSiftPylon(DataSiftConfig config) {
        super(config);
    }

    /**
     * Validate the given CSDL string. For information on this endpoint see documentation page:
     * http://dev.datasift.com/pylon/docs/api/pylon-api-endpoints/pylonvalidate
     *
     * @param csdl the CSDL to validate
     * @return the results of the validation, use {@link com.datasift.client.core.Validation#isValid()} to check if
     * validation was successful
     */
    public FutureData<PylonValidation> validate(String csdl) {
        FutureData<PylonValidation> future = new FutureData<PylonValidation>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(VALIDATE));
        JSONRequest request = config.http().postJSON(
                uri, new PageReader(newRequestCallback(future, new PylonValidation(), config)))
                .addField("csdl", csdl);
        performRequest(future, request);
        return future;
    }

    /**
     * Compile a CSDL string to a stream hash to which you can later subscribe and receive interactions from.
     * For information on this endpoint see documentation page:
     * http://dev.datasift.com/pylon/docs/api/pylon-api-endpoints/pyloncompile
     *
     * @param csdl the CSDL to compile
     * @return a stream object representing the DataSift compiled CSDL, use {@link com.datasift.client.core
     * .Stream#hash()}
     * to list the hash for the compiled CSDL
     */
    public FutureData<PylonStream> compile(String csdl) {
        FutureData<PylonStream> future = new FutureData<PylonStream>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(COMPILE));
        JSONRequest request = config.http().postJSON(
                uri, new PageReader(newRequestCallback(future, new PylonStream(), config)))
                .addField("csdl", csdl);
        performRequest(future, request);
        return future;
    }

    /**
     * Start the stream with the given hash. For information on this endpoint see documentation page:
     * http://dev.datasift.com/pylon/docs/api/pylon-api-endpoints/pylonstart
     *
     * @param hash the stream hash
     * @return a result which can be checked for success or failure, A status 204 indicates success,
     * or using {@link com.datasift.client.BaseDataSiftResult#isSuccessful()}
     */
    public FutureData<DataSiftResult> start(String hash) {
        return start(hash, null);
    }

    /**
     * Start the stream with the given hash & name. For information on this endpoint see documentation page:
     * http://dev.datasift.com/pylon/docs/api/pylon-api-endpoints/pylonstart
     *
     * @param hash the stream hash
     * @param name a name for the subscription
     * @return a result which can be checked for success or failure, A status 204 indicates success,
     * or using {@link com.datasift.client.BaseDataSiftResult#isSuccessful()}
     */
    public FutureData<DataSiftResult> start(String hash, String name) {
        if (hash == null || hash.isEmpty()) {
            throw new IllegalArgumentException("A valid hash is required to start a stream");
        }
        FutureData<DataSiftResult> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(START));
        JSONRequest request = config.http()
                .putJSON(uri, new PageReader(newRequestCallback(future, new BaseDataSiftResult(), config)))
                .addField("hash", hash)
                .addField("name", name);
        performRequest(future, request);
        return future;
    }

    /**
     * Stop the stream with the given hash. For information on this endpoint see documentation page:
     * http://dev.datasift.com/pylon/docs/api/pylon-api-endpoints/pylonstop
     *
     * @param hash the hash for the stream to stop
     * @return a result which can be checked for success or failure, A status 204 indicates success,
     * or using {@link com.datasift.client.BaseDataSiftResult#isSuccessful()}
     */
    public FutureData<DataSiftResult> stop(String hash) {
        if (hash == null || hash.isEmpty()) {
            throw new IllegalArgumentException("A valid hash is required to stop a stream");
        }
        FutureData<DataSiftResult> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(STOP));
        JSONRequest request = config.http()
                .putJSON(uri, new PageReader(newRequestCallback(future, new BaseDataSiftResult(), config)))
                .addField("hash", hash);
        performRequest(future, request);
        return future;
    }

    /**
     * Get the status of all streams. For information on this endpoint see documentation page:
     * http://dev.datasift.com/pylon/docs/api/pylon-api-endpoints/pylonget
     * @return the status of all streams that are running or have run with stored data
     */
    public FutureData<PylonStreamStatusList> get() {
        return get(0, 0);
    }

    /**
     * Get the status of all streams on page given. For information on this endpoint see documentation page:
     * http://dev.datasift.com/pylon/docs/api/pylon-api-endpoints/pylonget
     * @return the status of all streams that are running or have run with stored data
     */
    public FutureData<PylonStreamStatusList> get(int page) {
        return get(page, 0);
    }

    /**
     * Get the status of all streams on page given. For information on this endpoint see documentation page:
     * http://dev.datasift.com/pylon/docs/api/pylon-api-endpoints/pylonget
     * @return the status of all streams that are running or have run with stored data
     */
    public FutureData<PylonStreamStatusList> get(int page, int perPage) {
        FutureData<PylonStreamStatusList> future = new FutureData<>();
        ParamBuilder b = new ParamBuilder();
        if (page > 0) {
            b.put("page", page);
        }
        if (perPage > 0) {
            b.put("per_page", perPage);
        }
        URI uri = b.forURL(config.newAPIEndpointURI(GET));
        Request request = config.http().GET(uri,
                new PageReader(newRequestCallback(future, new PylonStreamStatusList(), config)));
        performRequest(future, request);
        return future;
    }

    /**
     * Get the status of the stream with a given hash. For information on this endpoint see documentation page:
     * http://dev.datasift.com/pylon/docs/api/pylon-api-endpoints/pylonget
     *
     * @param hash A stream hash
     * @return the status of the requested stream
     */
    public FutureData<PylonStreamStatus> get(String hash) {
        URI uri = newParams().put("hash", hash).forURL(config.newAPIEndpointURI(GET));
        FutureData<PylonStreamStatus> future = new FutureData<>();
        Request request = config.http().GET(uri,
                new PageReader(newRequestCallback(future, new PylonStreamStatus(), config)));
        performRequest(future, request);
        return future;
    }

    /**
     * Analyze a given recording and retrieve results. For information on this endpoint see documentation page:
     * http://dev.datasift.com/pylon/docs/api/pylon-api-endpoints/pylonanalyze
     *
     * @param query pylon options for a stream
     * @return information on execution of a stream
     */
    public FutureData<PylonResult> analyze(PylonQuery query) {
        if (query == null) {
            throw new IllegalArgumentException("A valid analyze request body is required to analyze a stream");
        }
        FutureData<PylonResult> future = new FutureData<PylonResult>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(ANALYZE));
        try {
            JSONRequest result = config.http()
                    .postJSON(uri, new PageReader(newRequestCallback(future, new PylonResult(), config)))
                    .setData(query);
            performRequest(future, result);
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException("Valid JSON is required to analyze a stream");
        }
        return future;
    }

    /**
     * Retrieve VEDO tags for a given filter hash. For information on this endpoint see documentation page:
     * http://dev.datasift.com/pylon/docs/api/pylon-api-endpoints/pylontags
     *
     * @param hash A filter hash
     * @return vedo tags for the given filter
     */
    public FutureData<PylonTags> tags(String hash) {
        URI uri = newParams().put("hash", hash).forURL(config.newAPIEndpointURI(TAGS));
        FutureData<PylonTags> future = new FutureData<>();
        Request request = config.http().GET(uri,
                new PageReader(newRequestCallback(future, new PylonTags(), config)));
        performRequest(future, request);
        return future;
    }

    /**
     * Sample a Pylon recording. For information on this endpoint see documentation page:
     * http://dev.datasift.com/pylon/docs/api/pylon-api-endpoints/pylonsample
     *
     * @param sampleRequest Request object containing parameters for Sample
     * @return PylonSample object containing results of sampling
     */
    public FutureData<PylonSample> sample(PylonSampleRequest sampleRequest) {
        if (sampleRequest == null) {
            throw new IllegalArgumentException("A valid sample request object is required to carry out a Pylon sample");
        }
        FutureData<PylonSample> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(SAMPLE));
        try {
            JSONRequest result = config.http()
                    .postJSON(uri, new PageReader(newRequestCallback(future, new PylonSample(), config)))
                    .setData(sampleRequest);
            performRequest(future, result);
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException("Valid JSON is required to analyze a stream");
        }
        return future;
    }
}
