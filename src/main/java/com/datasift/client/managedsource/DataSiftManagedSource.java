package com.datasift.client.managedsource;

import com.datasift.client.DataSiftAPIResult;
import com.datasift.client.DataSiftApiClient;
import com.datasift.client.DataSiftClient;
import com.datasift.client.DataSiftConfig;
import com.datasift.client.DataSiftResult;
import com.datasift.client.FutureData;
import com.datasift.client.FutureResponse;
import com.datasift.client.ParamBuilder;
import com.datasift.client.managedsource.sources.BaseSource;
import com.datasift.client.managedsource.sources.DataSource;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.higgs.http.client.POST;
import io.higgs.http.client.Request;
import io.higgs.http.client.readers.PageReader;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Courtney Robinson <courtney@crlog.info>
 */
public class DataSiftManagedSource extends DataSiftApiClient {
    public static final String CREATE = "source/create", UPDATE = "source/update", START = "source/start",
            STOP = "source/stop", DELETE = "source/delete", GET = "source/get", LOG = "source/log",
            ADD_AUTH = "source/auth/add", REMOVE_AUTH = "source/auth/remove",
            ADD_RESOURCE = "source/resource/add", REMOVE_RESOURCE = "source/resource/remove";

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

    public <T extends DataSource> FutureData<ManagedSource> create(String name, T source, boolean validate) {
        return updateOrCreate(name, source, null, validate);
    }

    public FutureData<ManagedSource> addAuth(String id, String... resources) {
        return addAuth(id, true, resources);
    }

    /**
     * Add one or more authentication credentials to a given managed source
     *
     * @param id        the ID of the source
     * @param validate  if true each token is validated
     * @param resources a set of tokens
     * @return the source
     */
    public FutureData<ManagedSource> addAuth(String id, boolean validate, String... resources) {
        if (id == null || resources == null || resources.length == 0) {
            throw new IllegalArgumentException("ID and a resource is required");
        }
        FutureData<ManagedSource> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(ADD_AUTH));
        try {
            List<BaseSource.ResourceParams> list = new ArrayList<>();
            for (String val : resources) {
                BaseSource.ResourceParams params = new ManagedSource.ResourceParams();
                params.set("value", val);
                list.add(params);
            }
            POST request = config.http().POST(uri, new PageReader(newRequestCallback(future,
                    new ManagedSource(), config)))
                    .form("id", id)
                    .form("auth", DataSiftClient.MAPPER.writeValueAsString(list))
                    .form("validate", DataSiftClient.MAPPER.writeValueAsString(Arrays.asList(validate)));
            performRequest(future, request);
        } catch (JsonProcessingException jpe) {
            future.interuptCause(jpe);
            future.doNotify();
        }
        return future;
    }

    public FutureData<ManagedSource> removeAuth(String id, String... resources) {
        if (id == null || resources == null || resources.length == 0) {
            throw new IllegalArgumentException("ID and a resource is required");
        }
        FutureData<ManagedSource> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(REMOVE_AUTH));
        try {
            POST request = config.http().POST(uri, new PageReader(newRequestCallback(future,
                    new ManagedSource(), config)))
                    .form("id", id)
                    .form("auth_ids", DataSiftClient.MAPPER.writeValueAsString(resources));
            performRequest(future, request);
        } catch (JsonProcessingException jpe) {
            future.interuptCause(jpe);
            future.doNotify();
        }
        return future;
    }

    /**
     * Add a resource to a given managed source
     *
     * @param id        the ID of the source to add to
     * @param validate  whether to validate the resources
     * @param resources set of resources to add
     * @return the managed source
     */
    public FutureData<ManagedSource> addResource(String id, boolean validate, BaseSource.ResourceParams... resources) {
        if (id == null || resources == null || resources.length == 0) {
            throw new IllegalArgumentException("ID and oen or more resources are required");
        }
        FutureData<ManagedSource> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(ADD_RESOURCE));
        POST request = null;
        try {
            request = config.http().POST(uri, new PageReader(newRequestCallback(future, new ManagedSource(), config)))
                    .form("id", id)
                    .form("resources", DataSiftClient.MAPPER.writeValueAsString(resources))
                    .form("validate", validate);
        } catch (JsonProcessingException e) {
            future.interuptCause(e);
            future.doNotify();
        }
        performRequest(future, request);
        return future;
    }

    /**
     * Remove a set of resources from a managed source
     *
     * @param id        the ID of the managed source
     * @param resources the resources to remove
     * @return the managed source
     */
    public FutureData<ManagedSource> removeResource(String id, String... resources) {
        if (id == null || resources == null || resources.length == 0) {
            throw new IllegalArgumentException("ID and oen or more resources are required");
        }
        FutureData<ManagedSource> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(REMOVE_RESOURCE));
        POST request = null;
        try {
            request = config.http().POST(uri, new PageReader(newRequestCallback(future, new ManagedSource(), config)))
                    .form("id", id)
                    .form("resource_ids", DataSiftClient.MAPPER.writeValueAsString(resources));
        } catch (JsonProcessingException e) {
            future.interuptCause(e);
            future.doNotify();
        }
        performRequest(future, request);
        return future;
    }

    /**
     * Update an existing managed source
     *
     * @param name   the name of the source
     * @param source the source and its configuration
     * @return this
     */
    public <T extends DataSource> FutureData<ManagedSource> update(String name, T source, ManagedSource id) {
        return update(name, source, id, false);
    }
    public <T extends DataSource> FutureData<ManagedSource> update(String name, T source, ManagedSource id,
                                                                   Boolean validate) {
        if (id == null) {
            throw new IllegalArgumentException("An existing managed source is required");
        }
        return updateOrCreate(name, source, id.getId(), validate);
    }

    protected <T extends DataSource> FutureData<ManagedSource> updateOrCreate(String name, T source, String id) {
        return updateOrCreate(name, source, id, false);
    }

    protected <T extends DataSource> FutureData<ManagedSource> updateOrCreate(String name, T source, String id,
                                                                              Boolean validate) {
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
        if (validate != null) {
            request.form("validate", validate);
        }
        performRequest(future, request);
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
        final DataSiftResult res = new DataSiftAPIResult();
        unwrapFuture(source, future, res, new FutureResponse<ManagedSource>() {
            public void apply(ManagedSource data) {
                URI uri = newParams().forURL(config.newAPIEndpointURI(START));
                POST request = config.http().POST(uri, new PageReader(newRequestCallback(future, data, config)))
                        .form("id", data.getId());
                performRequest(future, request);
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
        performRequest(future, request);
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
        POST request = config.http()
                .POST(uri, new PageReader(newRequestCallback(future, new DataSiftAPIResult(), config)))
                .form("id", id);
        performRequest(future, request);
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
        performRequest(future, request);
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
        performRequest(future, request);
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
        performRequest(future, request);
        return future;
    }

}
