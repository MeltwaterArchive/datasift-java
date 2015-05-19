package com.datasift.client.accounts;

import com.datasift.client.BaseDataSiftResult;
import com.datasift.client.DataSiftApiClient;
import com.datasift.client.DataSiftConfig;
import com.datasift.client.DataSiftResult;
import com.datasift.client.FutureData;
import com.datasift.client.ParamBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
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

    /**
     * Create a new Identity
     *
     * @param label  text label to tag the identity with
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

    /**
     * Update an existing identity with values
     *
     * @param id     target to update
     * @param label  new label (may be null otherwise)
     * @param active new activity (may be null otherwise)
     * @param master new master (may be null otherwise)
     * @return the new updated Identity
     */
    public FutureData<Identity> update(String id, String label, boolean active, boolean master) {
        String activeStr = active ? "active" : "disabled";
        FutureData<Identity> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(IDENTITY + "/" + id));
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

    /**
     * List identities with a given label and page details
     *
     * @param label   which label you'd like to list (can be null)
     * @param page    page number (can be 0)
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

    /**
     * Fetch an Identity using it's ID
     *
     * @param id the ID of the identity to fetch
     * @return the identity for the ID provided
     */
    public FutureData<Identity> get(String id) {
        FutureData<Identity> future = new FutureData<>();
        URI uri = newParams().put("id", id).forURL(config.newAPIEndpointURI(IDENTITY + "/" + id));
        Request request = config.http().
                GET(uri, new PageReader(newRequestCallback(future, new Identity(), config)));
        performRequest(future, request);
        return future;
    }

    /**
     * Delete an Identity using it's ID
     *
     * @param id the ID of the identity to delete
     * @return success or failure
     */
    public FutureData<DataSiftResult> delete(String id) {
        if (id == null) {
            throw new IllegalArgumentException("An identity is required");
        }
        FutureData<DataSiftResult> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(IDENTITY + "/" + id));
        Request request = config.http()
                .DELETE(uri, new PageReader(newRequestCallback(future, new BaseDataSiftResult(), config)));
        performRequest(future, request);
        return future;
    }

    // Token functions
    public FutureData<TokenList> listTokens(String identity) {
        return listTokens(identity, 0, 0);
    }

    public FutureData<TokenList> listTokens(String identity, int page) {
        return listTokens(identity, page, 0);
    }

    /**
     * List tokens associated with an identity
     *
     * @param identity which identity you want to list the tokens of
     * @param page     page number (can be 0)
     * @param perPage  items per page (can be 0)
     * @return List of identities
     */
    public FutureData<TokenList> listTokens(String identity, int page, int perPage) {
        if (identity == null) {
            throw new IllegalArgumentException("An identity is required");
        }
        FutureData<TokenList> future = new FutureData<>();
        ParamBuilder b = newParams();
        if (page > 0) {
            b.put("page", page);
        }
        if (perPage > 0) {
            b.put("per_page", perPage);
        }
        URI uri = b.forURL(config.newAPIEndpointURI(IDENTITY + "/" + identity + "/token"));
        Request request = config.http().
                GET(uri, new PageReader(newRequestCallback(future, new TokenList(), config)));
        performRequest(future, request);
        return future;
    }

    /**
     * Fetch a token using it's ID and it's Identity's ID
     *
     * @param identity the ID of the identity to query
     * @param tokenid  the ID of the token to fetch
     * @return the identity for the ID provided
     */
    public FutureData<Token> getToken(String identity, String tokenid) {
        FutureData<Token> future = new FutureData<>();
        URI uri = newParams().put("id", identity)
                .forURL(config.newAPIEndpointURI(IDENTITY + "/" + identity + "/" + tokenid));
        Request request = config.http().
                GET(uri, new PageReader(newRequestCallback(future, new Token(), config)));
        performRequest(future, request);
        return future;
    }

    /**
     * Create a new token
     *
     * @param identity identity to store this token under
     * @param service  service name to give this token
     * @param token    token
     * @return created Token
     */
    public FutureData<Token> createToken(String identity, String service, String token) {
        if (service == null || service.isEmpty()) {
            throw new IllegalArgumentException("A service is required");
        }
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("A token is required");
        }
        FutureData<Token> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(IDENTITY + "/" + identity + "/token"));
        try {
            Request request = config.http()
                    .postJSON(uri, new PageReader(newRequestCallback(future, new Token(), config)))
                    .setData(new NewToken(service, token));
            performRequest(future, request);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return future;
    }

    /**
     * Delete a service token
     *
     * @param identity identity to delete the token from
     * @param service  service to delete the token from
     * @return Success of deletion
     */
    public FutureData<DataSiftResult> deleteToken(String identity, String service) {
        if (identity == null) {
            throw new IllegalArgumentException("An identity is required");
        }
        if (service == null) {
            throw new IllegalArgumentException("A service is required");
        }
        FutureData<DataSiftResult> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(IDENTITY + "/" + identity + "/token/" + service));
        Request request = config.http()
                .DELETE(uri, new PageReader(newRequestCallback(future, new BaseDataSiftResult(), config)));
        performRequest(future, request);
        return future;
    }

    /**
     * Update a token
     * @param identity identity to update a token inside
     * @param service service to update the token for
     * @param token new token value
     * @return the updated Token
     */
    public FutureData<Token> updateToken(String identity, String service, String token) {
        if (identity == null || identity.isEmpty()) {
            throw new IllegalArgumentException("An identity is required");
        }
        if (service == null || service.isEmpty()) {
            throw new IllegalArgumentException("A service is required");
        }
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("A token is required");
        }
        FutureData<Token> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(IDENTITY + "/" + identity + "/token/" + service));
        try {
            Request request = config.http()
                    .putJSON(uri, new PageReader(newRequestCallback(future, new Token(), config)))
                    .setData(new NewTokenValue(token));
            performRequest(future, request);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return future;
    }

    // limits

     /**
     * Fetch a Limit
     *
     * @param identity the ID of the identity
     * @param service the name of the service
     * @return the limit for the service in that identity
     */
    public FutureData<Limit> getLimit(String identity, String service) {
        FutureData<Limit> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(IDENTITY + "/" + identity + "/limit/" + service));
        Request request = config.http().
                GET(uri, new PageReader(newRequestCallback(future, new Limit(), config)));
        performRequest(future, request);
        return future;
    }

    /**
     * Create a Limit
     *
     * @param identity ID of the identity to store the limit in
     * @param service service to set the limit for
     * @param allowance allowance to store in the limit
     * @return created limit
     */
    public FutureData<Limit> createLimit(String identity, String service, Long allowance) {
        if (identity == null || identity.isEmpty()) {
            throw new IllegalArgumentException("An identity is required");
        }
        if (service == null || service.isEmpty()) {
            throw new IllegalArgumentException("A service is required");
        }
        if (allowance < 0) {
            throw new IllegalArgumentException("Allowance must be a positive integer");
        }
        FutureData<Limit> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(IDENTITY + "/" + identity + "/limit"));
        try {
            Request request = config.http()
                    .postJSON(uri, new PageReader(newRequestCallback(future, new Limit(), config)))
                    .setData(new NewLimit(service, allowance));
            performRequest(future, request);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return future;
    }

    public FutureData<LimitList> listLimits(String service) {
        return listLimits(service, 0, 0);
    }

    public FutureData<LimitList> listLimits(String service, int page) {
        return listLimits(service, page, 0);
    }

     /**
     * List limits for the given service
     *
     * @param service  which service you want to list the limits of
     * @param page     page number (can be 0)
     * @param perPage  items per page (can be 0)
     * @return List of identities
     */
    public FutureData<LimitList> listLimits(String service, int page, int perPage) {
        if (service == null || service.isEmpty()) {
            throw new IllegalArgumentException("A service is required");
        }
        FutureData<LimitList> future = new FutureData<>();
        ParamBuilder b = newParams();
        if (page > 0) {
            b.put("page", page);
        }
        if (perPage > 0) {
            b.put("per_page", perPage);
        }
        URI uri = b.forURL(config.newAPIEndpointURI(IDENTITY + "/limit/" + service));
        Request request = config.http().
                GET(uri, new PageReader(newRequestCallback(future, new LimitList(), config)));
        performRequest(future, request);
        return future;
    }

     /**
     * Delete a limit
     *
     * @param identity identity to delete the limit from
     * @param service  service to delete the limit from
     * @return Success of deletion
     */
    public FutureData<DataSiftResult> deleteLimit(String identity, String service) {
        if (identity == null) {
            throw new IllegalArgumentException("An identity is required");
        }
        if (service == null) {
            throw new IllegalArgumentException("A service is required");
        }
        FutureData<DataSiftResult> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(IDENTITY + "/" + identity + "/limit/" + service));
        Request request = config.http()
                .DELETE(uri, new PageReader(newRequestCallback(future, new BaseDataSiftResult(), config)));
        performRequest(future, request);
        return future;
    }

    /**
     * Update a token
     * @param identity identity to update a token inside
     * @param service service to update the token for
     * @param allowance new limit value
     * @return the updated Token
     */
    public FutureData<Limit> updateLimit(String identity, String service, Long allowance) {
        if (identity == null || identity.isEmpty()) {
            throw new IllegalArgumentException("An identity is required");
        }
        if (service == null || service.isEmpty()) {
            throw new IllegalArgumentException("A service is required");
        }
        if (allowance < 0) {
            throw new IllegalArgumentException("Allowance must be a positive integer");
        }
        FutureData<Limit> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(IDENTITY + "/" + identity + "/limit/" + service));
        try {
            Request request = config.http()
                    .putJSON(uri, new PageReader(newRequestCallback(future, new Limit(), config)))
                    .setData(new NewLimitValue(allowance));
            performRequest(future, request);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return future;
    }

}
