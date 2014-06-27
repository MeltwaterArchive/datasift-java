package com.datasift.client.dynamiclist;

import com.datasift.client.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.higgs.http.client.Request;
import io.higgs.http.client.readers.PageReader;

import java.net.URI;
import java.util.List;

/**
 * @author Christopher Gilbert <christopher.john.gilbert@gmail.com>
 */
public class DataSiftDynamicList extends DataSiftApiClient {
    public static final String GET = "list/get", CREATE = "list/create", DELETE = "list/delete",
            EXISTS = "list/exists", ADD = "list/add", REMOVE = "list/remove";

    public enum ListType {
        STRING,
        INTEGER;

        @Override
        public String toString() {
            switch (this) {
                case STRING:
                    return "text";
                case INTEGER:
                    return "integer";
            }
            return super.toString();
        }
    }

    public DataSiftDynamicList(DataSiftConfig config) {
        super(config);
    }

    /**
     * Retrieve all lists owned by the user given by the API credentials
     *
     * @return this
     */
    public FutureData<DataSiftResult> get() {
        final FutureData<DataSiftResult> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(GET));
        Request request = config.http()
                .GET(uri, new PageReader(newRequestCallback(future, new BaseDataSiftResult(), config)));
        performRequest(future, request);
        return future;
    }

    /**
     * Create a new dynamic list with the given name and type
     *
     * @param type the type of the list to create
     * @param name a user specified string to identify the list
     * @return this
     */
    public FutureData<DynamicList> create(ListType type, String name) {
        if (type == null || name == null) {
            throw new IllegalArgumentException("Type and name are both required");
        }
        final FutureData<DynamicList> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(CREATE));
        Request request = config.http()
                .POST(uri, new PageReader(newRequestCallback(future, new DynamicList(), config)))
                .form("type", type.toString())
                .form("name", name);
        performRequest(future, request);
        return future;
    }

    /**
     * Delete a dynamic list with the given id
     *
     * @param list the list to delete
     * @return this
     */
    public FutureData<DataSiftResult> delete(DynamicList list) {
        if (list == null || list.getId() == null) {
            throw new IllegalArgumentException("Id is required");
        }
        final FutureData<DataSiftResult> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(DELETE));
        Request request = config.http()
                .POST(uri, new PageReader(newRequestCallback(future, new BaseDataSiftResult(), config)))
                .form("id", list.getId());
        performRequest(future, request);
        return future;
    }

    /**
     * Check if one or more items exist in a dynamic list with the given id
     *
     * @param list the list to check for items
     * @param items the list of items to check
     * @param <T> the type of the items to check, may be either string or integer, and must match the type of the list
     * @return this
     */
    public <T> FutureData<DataSiftResult> exists(DynamicList list, List<T> items) {
        if (list == null || list.getId() == null) {
            throw new IllegalArgumentException("Id and items are both required");
        }
        final FutureData<DataSiftResult> future = new FutureData<>();
        final ObjectMapper mapper = new ObjectMapper();
        URI uri = newParams().forURL(config.newAPIEndpointURI(EXISTS));
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
     * Add one or more items to a dynamic list with the given id
     *
     * @param list the list to add items to
     * @param items the list of items to add
     * @param <T> the type of the items to add, may be either string or integer, and must match the type of the list
     * @return this
     */
    public <T> FutureData<DataSiftResult> add(DynamicList list, List<T> items) {
        if (list == null || list.getId() == null) {
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
     * Remove one or more items from a dynamic list with the given id
     *
     * @param list the list to remove items from
     * @param items the list of items to remove
     * @param <T> the type of the items to remove, may be either string or integer, and must match the type of the list
     * @return this
     */
    public <T> FutureData<DataSiftResult> remove(DynamicList list, List<T> items) {
        if (list == null || list.getId() == null) {
            throw new IllegalArgumentException("Id and items are both required");
        }
        final FutureData<DataSiftResult> future = new FutureData<>();
        final ObjectMapper mapper = new ObjectMapper();
        URI uri = newParams().forURL(config.newAPIEndpointURI(REMOVE));
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
}
