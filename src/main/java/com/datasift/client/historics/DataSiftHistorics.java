package com.datasift.client.historics;

import com.datasift.client.DataSiftApiClient;
import com.datasift.client.DataSiftConfig;
import com.datasift.client.DataSiftResult;
import com.datasift.client.FutureData;
import com.datasift.client.FutureResponse;
import io.higgs.http.client.POST;
import io.higgs.http.client.future.PageReader;
import org.joda.time.DateTime;

import java.net.URI;

import static java.util.concurrent.TimeUnit.MILLISECONDS;


/**
 * This class provides access to the DataSift Historics API.
 */
public class DataSiftHistorics extends DataSiftApiClient {
    public final String PREPARE = "historics/prepare", START = "historics/start", STOP = "historics/stop",
            UPDATE = "historics/update", STATUS = "historics/status", DELETE = "historics/delete",
            GET = "historics/list";

    public DataSiftHistorics(DataSiftConfig config) {
        super(config);
    }

    public FutureData<DataSiftResult> start(PreparedHistoricsQuery query) {
        return start(FutureData.wrap(query));
    }

    /**
     * Start the historics query given
     *
     * @return a result which can be checked for success or failure, A status 204 indicates success,
     *         or using {@link com.datasift.client.DataSiftResult#isSuccessful()}
     */
    public FutureData<DataSiftResult> start(FutureData<PreparedHistoricsQuery> query) {
        if (query == null) {
            throw new IllegalArgumentException("A valid PreparedHistoricsQuery is required");
        }
        final FutureData<DataSiftResult> future = new FutureData<>();
        DataSiftResult h = new DataSiftResult();

        FutureResponse<PreparedHistoricsQuery> r = new FutureResponse<PreparedHistoricsQuery>() {
            public void apply(PreparedHistoricsQuery data) {
                if (data.getId() == null || data.getId().isEmpty()) {
                    throw new IllegalArgumentException("A valid PreparedHistoricsQuery is required");
                }
                start(data.getId(), future);
            }
        };
        unwrapFuture(query, future, h, r);
        return future;
    }

    /**
     * Start the historics query with the given ID
     *
     * @param id the historics id
     * @return a result which can be checked for success or failure, A status 204 indicates success,
     *         or using {@link com.datasift.client.DataSiftResult#isSuccessful()}
     */
    public FutureData<DataSiftResult> start(String id) {
        return start(id, null);
    }

    protected FutureData<DataSiftResult> start(String id, FutureData<DataSiftResult> f) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("A valid ID is required to start a Historics query");
        }
        FutureData<DataSiftResult> future = f != null ? f : new FutureData<DataSiftResult>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(START));
        POST request = config.http().POST(uri, new PageReader(newRequestCallback(future, new DataSiftResult())))
                .form("id", id);
        applyConfig(request).execute();
        return future;
    }

    public FutureData<DataSiftResult> stop(PreparedHistoricsQuery query, String reason) {
        if (query == null || query.getId() == null || query.getId().isEmpty()) {
            throw new IllegalArgumentException("A valid PreparedHistoricsQuery is required");
        }
        return stop(query.getId(), reason);
    }

    /**
     * Stop a given historics query
     *
     * @param id     the historics ID
     * @param reason an optional ID
     * @return the results of calling the stop API
     */
    public FutureData<DataSiftResult> stop(String id, String reason) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("A valid ID is required to stop a Historics query");
        }
        FutureData<DataSiftResult> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(STOP));
        POST request = config.http().POST(uri, new PageReader(newRequestCallback(future, new DataSiftResult())))
                .form("id", id);
        if (reason != null) {
            request.form("reason", reason);
        }
        applyConfig(request).execute();
        return future;
    }

    public FutureData<DataSiftResult> delete(PreparedHistoricsQuery query) {
        if (query == null || query.getId() == null || query.getId().isEmpty()) {
            throw new IllegalArgumentException("A valid PreparedHistoricsQuery is required");
        }
        return delete(query.getId());
    }

    /**
     * Delete the historic with the given ID
     *
     * @param id an historic ID
     * @return a result indicating whether the request was successful or not
     */
    public FutureData<DataSiftResult> delete(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("A valid ID is required to delete a Historics query");
        }
        FutureData<DataSiftResult> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(DELETE));
        POST request = config.http().POST(uri, new PageReader(newRequestCallback(future, new DataSiftResult())))
                .form("id", id);
        applyConfig(request).execute();
        return future;
    }

    /**
     * Update the name of a historics query
     *
     * @param id   the ID of the historics to update
     * @param name the new name for the historics
     * @return a result that can be used to check the success or failure of the request
     */
    public FutureData<DataSiftResult> update(String id, String name) {
        if (id == null || name == null || id.isEmpty() || name.isEmpty()) {
            throw new IllegalArgumentException("A valid ID AND name is required to update a Historics query");
        }
        FutureData<DataSiftResult> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(UPDATE));
        POST request = config.http().POST(uri, new PageReader(newRequestCallback(future, new DataSiftResult())))
                .form("id", id)
                .form("name", name);
        applyConfig(request).execute();
        return future;
    }

    /**
     * Check the status of data availability in our archive for the given time period
     *
     * @param start   the dat from which the archive should be checked
     * @param end     the up to which the archive should be checked
     * @param sources an optional list of data sources that should be queried, e.g. [facebook,twitter,...]
     * @return a report of the current status/availability of data for the given time period
     */
    public FutureData<HistoricsStatus> status(DateTime start, DateTime end, String... sources) {
        FutureData<HistoricsStatus> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(STATUS));
        POST request = config.http().POST(uri, new PageReader(newRequestCallback(future, new HistoricsStatus())))
                .form("start", MILLISECONDS.toSeconds(start.getMillis()))
                .form("end", MILLISECONDS.toSeconds(end.getMillis()));
        if (sources != null && sources.length > 0) {
            StringBuilder b = new StringBuilder();
            for (String source : sources) {
                b.append(source).append(",");
            }
            request.form("sources", b.toString().substring(0, b.length() - 1));
        }
        applyConfig(request).execute();
        return future;
    }

    public FutureData<HistoricsQuery> get(String id) {
        return get(id, true);
    }

    /**
     * Get detailed information about a historics query
     *
     * @param id           the id of the historics to list
     * @param withEstimate if true then an estimated completion time is include in the response
     * @return a historics query
     */
    public FutureData<HistoricsQuery> get(String id, boolean withEstimate) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("ID is required get a historics query");
        }
        FutureData<HistoricsQuery> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(GET));
        POST request = config.http().POST(uri, new PageReader(newRequestCallback(future, new HistoricsQuery())))
                .form("id", id)
                .form("with_estimate", withEstimate ? 1 : 0);
        applyConfig(request).execute();
        return future;
    }

    public FutureData<HistoricsQueryList> list() {
        return list(100, 1, true);
    }

    public FutureData<HistoricsQueryList> list(int max, int page) {
        return list(max, page, true);
    }

    public FutureData<HistoricsQueryList> list(int page) {
        return list(100, page, true);
    }

    /**
     * Retrieve a list of {@link HistoricsQuery} objects
     *
     * @param max          max number of objects to list
     * @param page         a page number
     * @param withEstimate if true, include an estimated completion time
     * @return an iterable list of {@link HistoricsQuery}s
     */
    public FutureData<HistoricsQueryList> list(int max, int page, boolean withEstimate) {
        FutureData<HistoricsQueryList> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(GET));
        POST request = config.http().POST(uri, new PageReader(newRequestCallback(future, new HistoricsQueryList())))
                .form("with_estimate", withEstimate ? 1 : 0);
        if (max > 0) {
            request.form("max", max);
        }
        if (page > 0) {
            request.form("page", page);
        }
        applyConfig(request).execute();
        return future;
    }

    /**
     * @param hash  The hash of the CSDL for your historics query.
     *              Example values: 2459b03a13577579bca76471778a5c3d
     * @param start Unix timestamp for the start time.
     *              Example values: 1325548800
     * @param end   nix timestamp for the end time. Must be at least 24 in the past.
     *              Example values: 1325548800
     * @param name  The name you assign to your historics query.
     *              Example values: Football
     * @return the prepared Historics
     */
    public FutureData<PreparedHistoricsQuery> prepare(String hash, DateTime start, DateTime end, String name) {
        return prepare(hash, MILLISECONDS.toSeconds(start.getMillis()), MILLISECONDS.toSeconds(end.getMillis()),
                name, -1);
    }

    /**
     * @param hash    The hash of the CSDL for your historics query.
     *                Example values: 2459b03a13577579bca76471778a5c3d
     * @param start   Unix timestamp for the start time.
     *                Example values: 1325548800
     * @param end     nix timestamp for the end time. Must be at least 24 in the past.
     *                Example values: 1325548800
     * @param name    The name you assign to your historics query.
     *                Example values: Football
     * @param sources Comma-separated list of data sources to include. Currently,
     *                the only source you can use is twitter. In the future, you will be able to choose any source
     *                listed as a valid value that you would use in the interaction.type target.
     *                Example values: twitter
     * @return the prepared historics
     */
    public FutureData<PreparedHistoricsQuery> prepare(String hash, long start, long end, String name,
                                                      String... sources) {
        return prepare(hash, start, end, name, -1, sources);
    }

    public FutureData<PreparedHistoricsQuery> prepare(String hash, long start, long end, String name, int sample,
                                                      String... sources) {
        FutureData<PreparedHistoricsQuery> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(PREPARE));
        POST request = config.http().POST(uri, new PageReader(newRequestCallback(future, new PreparedHistoricsQuery())))
                .form("hash", hash)
                .form("start", start)
                .form("end", end)
                .form("name", name);
        if (sample > 0) {
            request.form("sample", sample);
        }
        if (sources == null || sources.length == 0) {
            sources = new String[]{"twitter"};
        }
        StringBuilder b = new StringBuilder();
        for (String source : sources) {
            b.append(source).append(",");
        }
        request.form("sources", b.toString().substring(0, b.length() - 1));
        applyConfig(request).execute();
        return future;
    }
}
