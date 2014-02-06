package com.datasift.client.managedsource;

import com.datasift.client.DataSiftApiClient;
import com.datasift.client.DataSiftConfig;
import com.datasift.client.DataSiftResult;
import com.datasift.client.FutureData;
import com.datasift.client.FutureResponse;
import com.datasift.client.ParamBuilder;
import com.datasift.client.managedsource.sources.DataSource;
import io.higgs.http.client.POST;
import io.higgs.http.client.Request;
import io.higgs.http.client.future.PageReader;

import java.net.URI;

/**
 * @author Courtney Robinson <courtney@crlog.info>
 */
public class DataSiftManagedSource extends DataSiftApiClient {
    public static final String CREATE = "source/create", UPDATE = "source/update", START = "source/start",
            STOP = "source/stop", DELETE = "source/delete", GET = "source/get", LOG = "source/log";

    public DataSiftManagedSource(DataSiftConfig config) {
        super(config);
    }

    /**
     * Create a new managed source
     *
     * @param name   the name of the source
     * @param source the source and its configuratiosn
     * @return this
     */
    public <T extends DataSource> FutureData<ManagedSource> create(String name, T source) {
        return updateOrCreate(name, source, null);
    }

    /**
     * Update an existing managed source
     *
     * @param name   the name of the source
     * @param source the source and its configuration
     * @return this
     */
    public <T extends DataSource> FutureData<ManagedSource> update(String name, T source, ManagedSource id) {
        if (id == null) {
            throw new IllegalArgumentException("An existing managed source is required");
        }
        return updateOrCreate(name, source, id.getId());
    }

    protected <T extends DataSource> FutureData<ManagedSource> updateOrCreate(String name, T source, String id) {
        if (name == null || source == null) {
            throw new IllegalArgumentException("Name and a data source are both required");
        }
        FutureData<ManagedSource> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(id == null ? CREATE : UPDATE));
        POST request = config.http().POST(uri, new PageReader(newRequestCallback(future, new ManagedSource(), config)))
                .form("source_type", source.type().value())
                .form("name", name);
        if (source.hasParams()) {
            request.form("parameters", source.getParametersAsJSON());
        }
        if (source.hasResources()) {
            request.form("resources", source.getResourcesAsJSON());
        }
        if (source.hasAuth()) {
            request.form("auth", source.getAuthAsJSON());
        }
        applyConfig(request).execute();
        return future;
    }

    public FutureData<DataSiftResult> start(String id) {
        return start(ManagedSource.fromString(id));
    }

    public FutureData<DataSiftResult> start(ManagedSource source) {
        return start(FutureData.wrap(source));
    }

    /**
     * @param source start a configured managed source
     * @return this
     */
    public FutureData<DataSiftResult> start(final FutureData<ManagedSource> source) {
        if (source == null) {
            throw new IllegalArgumentException("A data source is required");
        }
        final FutureData<DataSiftResult> future = new FutureData<>();
        final DataSiftResult res = new DataSiftResult();
        unwrapFuture(source, future, res, new FutureResponse<ManagedSource>() {
            public void apply(ManagedSource data) {
                URI uri = newParams().forURL(config.newAPIEndpointURI(START));
                POST request = config.http().POST(uri, new PageReader(newRequestCallback(future, data, config)))
                        .form("id", data.getId());
                applyConfig(request).execute();
            }
        });
        return future;
    }

    /**
     * @param id the ID of the managed source to stop
     * @return this
     */
    public FutureData<ManagedSource> stop(String id) {
        if (id == null) {
            throw new IllegalArgumentException("A data source is required");
        }
        FutureData<ManagedSource> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(STOP));
        POST request = config.http().POST(uri, new PageReader(newRequestCallback(future, new ManagedSource(), config)))
                .form("id", id);
        applyConfig(request).execute();
        return future;
    }

    /**
     * @param id the ID of the managed source to delete
     * @return this
     */
    public FutureData<DataSiftResult> delete(String id) {
        if (id == null) {
            throw new IllegalArgumentException("A data source is required");
        }
        FutureData<DataSiftResult> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(DELETE));
        POST request = config.http().POST(uri, new PageReader(newRequestCallback(future, new DataSiftResult(), config)))
                .form("id", id);
        applyConfig(request).execute();
        return future;
    }

    public FutureData<ManagedSourceList> get() {
        return get(null, 0, 0);
    }

    public FutureData<ManagedSourceList> get(int page) {
        return get(null, page, 0);
    }

    public FutureData<ManagedSourceList> get(int page, int perPage) {
        return get(null, page, perPage);
    }

    /**
     * Get manage sources for the given type
     */
    public FutureData<ManagedSourceList> get(ManagedDataSourceType type, int page, int perPage) {
        FutureData<ManagedSourceList> future = new FutureData<>();
        ParamBuilder b = newParams();
        if (type != null) {
            b.put("source_type", type.value());
        }
        if (page > 0) {
            b.put("page", page);
        }
        if (perPage > 0) {
            b.put("per_page", perPage);
        }
        URI uri = b.forURL(config.newAPIEndpointURI(GET));
        Request request = config.http().
                GET(uri, new PageReader(newRequestCallback(future, new ManagedSourceList(), config)));
        applyConfig(request).execute();
        return future;
    }

    /**
     * @param id the ID of the managed source to fetch
     * @return the managed source for the ID provided
     */
    public FutureData<ManagedSource> get(String id) {
        FutureData<ManagedSource> future = new FutureData<>();
        URI uri = newParams().put("id", id).forURL(config.newAPIEndpointURI(GET));
        Request request = config.http().
                GET(uri, new PageReader(newRequestCallback(future, new ManagedSource(), config)));
        applyConfig(request).execute();
        return future;
    }

    public FutureData<ManagedSourceLog> log(String id) {
        return log(id, 0, 0);
    }

    public FutureData<ManagedSourceLog> log(String id, int page) {
        return log(id, page, 0);
    }

    public FutureData<ManagedSourceLog> log(String id, int page, int perPage) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("A valid source ID is required");
        }
        FutureData<ManagedSourceLog> future = new FutureData<>();
        ParamBuilder b = newParams();
        if (page > 0) {
            b.put("page", page);
        }
        if (perPage > 0) {
            b.put("per_page", perPage);
        }
        URI uri = b.forURL(config.newAPIEndpointURI(LOG));
        Request request = config.http().
                GET(uri, new PageReader(newRequestCallback(future, new ManagedSourceLog(), config)));
        applyConfig(request).execute();
        return future;
    }

}
