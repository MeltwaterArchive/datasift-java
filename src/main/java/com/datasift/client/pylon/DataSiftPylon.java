package com.datasift.client.pylon;

import com.datasift.client.BaseDataSiftResult;
import com.datasift.client.DataSiftApiClient;
import com.datasift.client.DataSiftConfig;
import com.datasift.client.DataSiftResult;
import com.datasift.client.FutureData;
import com.datasift.client.ParamBuilder;
import com.datasift.client.pylon.PylonRecording.PylonRecordingId;
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
            STOP = "pylon/stop", UPDATE = "pylon/update", GET = "pylon/get", ANALYZE = "pylon/analyze",
            TAGS = "pylon/tags", SAMPLE = "pylon/sample";

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
     * @param stream the stream hash
     * @return a Datasift pylon recording id. See {@link com.datasift.client.pylon.PylonRecording.PylonRecordingId}
     */
    public FutureData<PylonRecordingId> start(PylonStream stream) {
        return start(stream, null);
    }

    /**
     * Start a recording with the given hash & name. For information on this endpoint see documentation page:
     * http://dev.datasift.com/pylon/docs/api/pylon-api-endpoints/pylonstart
     *
     * @param stream the stream hash
     * @param name a name for the subscription
     * @return a Datasift pylon recording id. See {@link com.datasift.client.pylon.PylonRecording.PylonRecordingId}
     */
    public FutureData<PylonRecordingId> start(PylonStream stream, String name) {
        if (stream == null || stream.hash.isEmpty()) {
            throw new IllegalArgumentException("A valid hash is required to start a stream");
        }
        FutureData<PylonRecordingId> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(START));
        JSONRequest request = config.http()
                .putJSON(uri, new PageReader(newRequestCallback(future, new PylonRecordingId(), config)))
                .addField("hash", stream.hash)
                .addField("name", name);
        performRequest(future, request);
        return future;
    }

    /**
     * Restart a recording using a recording id. For information on this endpoint see documentation page:
     * http://dev.datasift.com/pylon/docs/api/pylon-api-endpoints/pylonstart
     *
     * @param recordingId the recording id of a previously stopped recording.
     *                    See {@link com.datasift.client.pylon.PylonRecording.PylonRecordingId}
     * @return a result which can be checked for success or failure, A status 204 indicates success,
     * or using {@link com.datasift.client.BaseDataSiftResult#isSuccessful()}
     */
    public FutureData<DataSiftResult> restart(PylonRecordingId recordingId) {
        if (recordingId == null || recordingId.id == null || recordingId.id.isEmpty()) {
            throw new IllegalArgumentException("A valid recording id is required to restart a recording");
        }
        FutureData<DataSiftResult> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(START));
        JSONRequest request = config.http()
                .putJSON(uri, new PageReader(newRequestCallback(future, new BaseDataSiftResult(), config)))
                .addField("id", recordingId.getId());
        performRequest(future, request);
        return future;
    }

    public FutureData<DataSiftResult> update(PylonRecordingId recordingId, String newName) {
        return update(recordingId, null, newName);
    }

    public FutureData<DataSiftResult> update(PylonRecordingId recordingId, PylonStream stream) {
        return update(recordingId, stream, null);
    }

    public FutureData<DataSiftResult> update(PylonRecordingId recordingId, PylonStream stream, String newName) {
        if (recordingId == null || recordingId.id == null || recordingId.id.isEmpty()) {
            throw new IllegalArgumentException("A valid recording id is required to update a recording");
        }

        if ((stream == null || stream.hash == null || stream.hash.isEmpty()) && newName == null) {
            throw new IllegalArgumentException("One of stream or newName must be supplied for an update");
        }
        FutureData<DataSiftResult> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(UPDATE));
        JSONRequest request = config.http()
                .putJSON(uri, new PageReader(newRequestCallback(future, new BaseDataSiftResult(), config)))
                .addField("id", recordingId.id);

        if (stream != null) {
            request.addField("hash", stream.hash);
        }
        if (newName != null) {
            request.addField("name", newName);
        }
        performRequest(future, request);
        return future;
    }

    /**
     * Stop the stream with the given hash. For information on this endpoint see documentation page:
     * http://dev.datasift.com/pylon/docs/api/pylon-api-endpoints/pylonstop
     *
     * @param recordingId the id for the recording to stop.
     *                    See {@link com.datasift.client.pylon.PylonRecording.PylonRecordingId}
     * @return a result which can be checked for success or failure, A status 204 indicates success,
     * or using {@link com.datasift.client.BaseDataSiftResult#isSuccessful()}
     */
    public FutureData<DataSiftResult> stop(PylonRecordingId recordingId) {
        if (recordingId == null || recordingId.id == null || recordingId.id.isEmpty()) {
            throw new IllegalArgumentException("A valid recording id is required to stop a recording");
        }
        FutureData<DataSiftResult> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(STOP));
        JSONRequest request = config.http()
                .putJSON(uri, new PageReader(newRequestCallback(future, new BaseDataSiftResult(), config)))
                .addField("id", recordingId);
        performRequest(future, request);
        return future;
    }

    /**
     * Get the status of all recordings. For information on this endpoint see documentation page:
     * http://dev.datasift.com/pylon/docs/api/pylon-api-endpoints/pylonget
     * @return the status of all recordings that are running or have run with stored data
     */
    public FutureData<PylonRecordingList> get() {
        return get(0, 0);
    }

    /**
     * Get the status of all recordings on page given. For information on this endpoint see documentation page:
     * http://dev.datasift.com/pylon/docs/api/pylon-api-endpoints/pylonget
     * @return the status of all recordings that are running or have run with stored data
     */
    public FutureData<PylonRecordingList> get(int page) {
        return get(page, 0);
    }

    /**
     * Get the status of all recordings on page given. For information on this endpoint see documentation page:
     * http://dev.datasift.com/pylon/docs/api/pylon-api-endpoints/pylonget
     * @return the status of all recordings that are running or have run with stored data
     */
    public FutureData<PylonRecordingList> get(int page, int perPage) {
        FutureData<PylonRecordingList> future = new FutureData<>();
        ParamBuilder b = new ParamBuilder();
        if (page > 0) {
            b.put("page", page);
        }
        if (perPage > 0) {
            b.put("per_page", perPage);
        }
        URI uri = b.forURL(config.newAPIEndpointURI(GET));
        Request request = config.http().GET(uri,
                new PageReader(newRequestCallback(future, new PylonRecordingList(), config)));
        performRequest(future, request);
        return future;
    }

    /**
     * Get the status of the recording with a given id. For information on this endpoint see documentation page:
     * http://dev.datasift.com/pylon/docs/api/pylon-api-endpoints/pylonget
     *
     * @param recordingId id for the required recording.
     *                    See {@link com.datasift.client.pylon.PylonRecording.PylonRecordingId}
     * @return the status of the requested recording
     */
    public FutureData<PylonRecording> get(PylonRecordingId recordingId) {
        URI uri = newParams().put("id", recordingId.id).forURL(config.newAPIEndpointURI(GET));
        FutureData<PylonRecording> future = new FutureData<>();
        Request request = config.http().GET(uri,
                new PageReader(newRequestCallback(future, new PylonRecording(), config)));
        performRequest(future, request);
        return future;
    }

    /**
     * Analyze a given recording and retrieve results. For information on this endpoint see documentation page:
     * http://dev.datasift.com/pylon/docs/api/pylon-api-endpoints/pylonanalyze
     *
     * @param query pylon options for a recording
     * @return information on execution of a recording
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
     * Retrieve VEDO tags for a given recording. For information on this endpoint see documentation page:
     * http://dev.datasift.com/pylon/docs/api/pylon-api-endpoints/pylontags
     *
     * @param recordingId A recording id. See {@link com.datasift.client.pylon.PylonRecording.PylonRecordingId}
     * @return vedo tags for the given filter
     */
    public FutureData<PylonTags> tags(PylonRecordingId recordingId) {
        URI uri = newParams().put("id", recordingId).forURL(config.newAPIEndpointURI(TAGS));
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
        if (sampleRequest == null || sampleRequest.recordingId == null) {
            throw new IllegalArgumentException(
                    "A valid sample request object containing a recordingId is required to carry out a Pylon sample"
            );
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
