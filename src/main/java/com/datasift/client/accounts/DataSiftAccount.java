package com.datasift.client.accounts;

import com.datasift.client.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.higgs.http.client.POST;
import io.higgs.http.client.Request;
import io.higgs.http.client.readers.PageReader;

import java.net.URI;

/**
 * @author Courtney Robinson <courtney@crlog.info>
 */
public class DataSiftAccount extends DataSiftApiClient {
    public final String IDENTITY = "account/identity";

    public DataSiftAccount(DataSiftConfig config) {
        super(config);
    }

    public FutureData<Identity> create(String label) {
        return create(label, true, false);
    }

    public FutureData<Identity> create(String label, boolean active) {
        return create(label, active, false);
    }

    /***
     * Create a new Identity
     * @param label text label to tag the identity with
     * @param active whether the identity is active
     * @param master if the identity is a master identity
     * @return newly created Identity
     */
    public FutureData<Identity> create(String label, boolean active, boolean master) {
        if (label == null || label.isEmpty()) {
            throw new IllegalArgumentException("A label is required");
        }
        String activeStr = active ? "active" : "disabled";
        FutureData<Identity> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(IDENTITY));
        try {
            Request request = config.http()
                    .postJSON(uri, new PageReader(newRequestCallback(future, new Identity(), config)))
                    .setData(new NewIdentity(label, activeStr, master));
            performRequest(future, request);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return future;
    }

    /***
     * Update an existing identity with values
     * @param id target to update
     * @param label new label (may be null otherwise)
     * @param active new activity (may be null otherwise)
     * @param master new master (may be null otherwise)
     * @return the new updated Identity
     */
    public FutureData<Identity> update(String id, String label, boolean active, boolean master) {
        String activeStr = active ? "active" : "disabled";
        FutureData<Identity> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(IDENTITY+"/"+id));
        try {
            Request request = config.http()
                    .putJSON(uri, new PageReader(newRequestCallback(future, new Identity(), config)))
                    .setData(new NewIdentity(label, activeStr, master));
            performRequest(future, request);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return future;
    }

    public FutureData<IdentityList> list() {
        return list(null, 0, 0);
    }

    public FutureData<IdentityList> list(String label) {
        return list(label, 0, 0);
    }

    public FutureData<IdentityList> list(int page) {
        return list(null, page, 0);
    }

    public FutureData<IdentityList> list(String label, int page) {
        return list(label, page, 0);
    }

    public FutureData<IdentityList> list(int page, int perPage) {
        return list(null, page, perPage);
    }

    /***
     * List identities with a given label and page details
     * @param label which label you'd like to list (can be null)
     * @param page page number (can be 0)
     * @param perPage items per page (can be 0)
     * @return List of identities
     */
    public FutureData<IdentityList> list(String label, int page, int perPage) {
        FutureData<IdentityList> future = new FutureData<>();
        ParamBuilder b = newParams();
        if (label != null) {
            b.put("label", label);
        }
        if (page > 0) {
            b.put("page", page);
        }
        if (perPage > 0) {
            b.put("per_page", perPage);
        }
        URI uri = b.forURL(config.newAPIEndpointURI(IDENTITY));
        Request request = config.http().
                GET(uri, new PageReader(newRequestCallback(future, new IdentityList(), config)));
        performRequest(future, request);
        return future;
    }

    /***
     * Fetch an Identity using it's ID
     * @param id the ID of the identity to fetch
       @return the identity for the ID provided
     */
    public FutureData<Identity> get(String id) {
        FutureData<Identity> future = new FutureData<>();
        URI uri = newParams().put("id", id).forURL(config.newAPIEndpointURI(IDENTITY+"/"+id));
        Request request = config.http().
                GET(uri, new PageReader(newRequestCallback(future, new Identity(), config)));
        performRequest(future, request);
        return future;
    }

    /***
     * Delete an Identity using it's ID
     * @param id the ID of the identity to delete
     * @return success or failure
     */
    public FutureData<DataSiftResult> delete(String id) {
        if (id == null) {
            throw new IllegalArgumentException("An identity is required");
        }
        FutureData<DataSiftResult> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(IDENTITY+"/"+id));
        Request request = config.http()
                .DELETE(uri, new PageReader(newRequestCallback(future, new BaseDataSiftResult(), config)));
        performRequest(future, request);
        return future;
    }


}
