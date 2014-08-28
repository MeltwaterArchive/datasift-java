package com.datasift.client.dynamiclist.replace;

import com.datasift.client.*;
import com.datasift.client.dynamiclist.DynamicList;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.higgs.http.client.Request;
import io.higgs.http.client.readers.PageReader;

import java.net.URI;
import java.util.List;

/**
 * @author Christopher Gilbert <christopher.john.gilbert@gmail.com>
 */
public class DataSiftDynamicListReplace extends DataSiftApiClient {
    public static final String ADD = "list/replace/add", START = "list/replace/start",
            COMMIT = "list/replace/commit", ABORT = "list/replace/abort";

    public DataSiftDynamicListReplace(DataSiftConfig config) {
        super(config);
    }

    /**
     * Start a dynamic list replace
     *
     * @param list the id of the list for which to start the replace operation
     * @return this
     */
    public FutureData<ReplaceList> start(DynamicList list) {
        if (list == null || list.getId() == null) {
            throw new IllegalArgumentException("A valid list is required");
        }
        final FutureData<ReplaceList> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(START));
        Request request = config.http()
                .POST(uri, new PageReader(newRequestCallback(future, new ReplaceList(), config)))
                .form("list_id", list.getId());
        performRequest(future, request);
        return future;
    }

    /**
     * Add one or more items to a replace list with the given id
     *
     * @param list the replace list which to add items to
     * @param items the items to add to the replace list
     * @param <T> the type of the items to add, must be either string or integer, and must match the type of the list
     * @return this
     */
    public <T> FutureData<DataSiftResult> add(ReplaceList list, List<T> items) {
        if (list == null || list.getId() == null || items == null) {
            throw new IllegalArgumentException("Id and items are both required");
        }
        final FutureData<DataSiftResult> future = new FutureData<>();
        final ObjectMapper mapper = new ObjectMapper();
        URI uri = newParams().forURL(config.newAPIEndpointURI(ADD));
        try {
            Request request = config.http()
                    .POST(uri, new PageReader(newRequestCallback(future, new BaseDataSiftResult(), config)))
                    .form("id", list.getId())
                    .form("items", mapper.writeValueAsString(items));
            performRequest(future, request);
        } catch (JsonProcessingException e) {
            failNotify(future,e);
        }
        return future;
    }

    /**
     * Commit the replace list with the given id
     *
     * @param list the replace list to commit
     * @return this
     */
    public FutureData<DataSiftResult> commit(ReplaceList list) {
        if (list == null || list.getId() == null) {
            throw new IllegalArgumentException("Id is required");
        }
        final FutureData<DataSiftResult> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(COMMIT));
        Request request = config.http()
                .POST(uri, new PageReader(newRequestCallback(future, new BaseDataSiftResult(), config)))
                .form("id", list.getId());
        performRequest(future, request);
        return future;
    }

    /**
     * Abort the replace list
     *
     * @param list the replace list to abort
     * @return this
     */
    public FutureData<DataSiftResult> abort(ReplaceList list) {
        if (list == null || list.getId() == null) {
            throw new IllegalArgumentException("Id is required");
        }
        final FutureData<DataSiftResult> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(ABORT));
        Request request = config.http()
                .POST(uri, new PageReader(newRequestCallback(future, new BaseDataSiftResult(), config)))
                .form("id", list.getId());
        performRequest(future, request);
        return future;
    }
}
