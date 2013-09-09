package com.datasift.client.push;

import com.datasift.client.DataSiftApiClient;
import com.datasift.client.DataSiftClient;
import com.datasift.client.DataSiftConfig;
import com.datasift.client.DataSiftResult;
import com.datasift.client.FutureData;
import com.datasift.client.FutureResponse;
import com.datasift.client.core.Stream;
import com.datasift.client.historics.HistoricsQuery;
import com.datasift.client.historics.PreparedHistoricsQuery;
import com.datasift.client.push.connectors.PushConnector;
import com.datasift.client.stream.Interaction;
import com.fasterxml.jackson.databind.JavaType;
import io.higgs.core.func.Function2;
import io.higgs.http.client.POST;
import io.higgs.http.client.Response;
import io.higgs.http.client.future.PageReader;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * @author Courtney Robinson <courtney@crlog.info>
 */
public class DataSiftPush extends DataSiftApiClient {
    public final String VALIDATE = "push/validate", CREATE = "push/create", PAUSE = "push/pause",
            RESUME = "push/resume", UPDATE = "push/update", STOP = "push/stop", DELETE = "push/delete",
            LOG = "push/log", GET = "push/get", PULL = "push/pull";

    public DataSiftPush(DataSiftConfig config) {
        super(config);
    }

    /**
     * Pause the push subscription with the given ID
     *
     * @param id the id of the push subscription to pause
     * @return the push subscription that has been paused
     */
    public FutureData<PushSubscription> pause(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("A push subscription ID is required");
        }
        FutureData<PushSubscription> future = new FutureData<PushSubscription>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(PAUSE));
        POST request = config.http().POST(uri, new PageReader(newRequestCallback(future, new PushSubscription())))
                .form("id", id);
        applyConfig(request).execute();
        return future;
    }

    /**
     * Set a paused subscription to run again
     *
     * @param id the id of the push subscription to resume
     * @return the push subscription
     */
    public FutureData<PushSubscription> resume(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("A push subscription ID is required");
        }
        FutureData<PushSubscription> future = new FutureData<PushSubscription>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(RESUME));
        POST request = config.http().POST(uri, new PageReader(newRequestCallback(future, new PushSubscription())))
                .form("id", id);
        applyConfig(request).execute();
        return future;
    }

    /**
     * Stop/cancel a given push subscription
     *
     * @param id the id of the push subscription
     * @return the subscription that was stopped
     */
    public FutureData<PushSubscription> stop(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("A push subscription ID is required");
        }
        FutureData<PushSubscription> future = new FutureData<PushSubscription>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(STOP));
        POST request = config.http().POST(uri, new PageReader(newRequestCallback(future, new PushSubscription())))
                .form("id", id);
        applyConfig(request).execute();
        return future;
    }

    /**
     * Delete a push subsctiption
     *
     * @param id the id of the subscription to delete
     * @return a DataSiftResult which can be checked for success or failure of the request
     */
    public FutureData<DataSiftResult> delete(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("A push subscription ID is required");
        }
        FutureData<DataSiftResult> future = new FutureData<DataSiftResult>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(DELETE));
        POST request = config.http().POST(uri, new PageReader(newRequestCallback(future, new DataSiftResult())))
                .form("id", id);
        applyConfig(request).execute();
        return future;
    }

    public FutureData<PushSubscription> update(String id, PushConnector connector) {
        return update(id, connector, null);
    }

    /**
     * Updates the name or output parameters for a push sucription
     *
     * @param id        the subscription ID
     * @param connector the output parameters to update to
     * @param name      an optional name to update with
     * @return the updated push subscription
     */
    public FutureData<PushSubscription> update(String id, PushConnector connector, String name) {
        if (id == null || id.isEmpty() || connector == null) {
            throw new IllegalArgumentException("A push subscription ID and output parameters is required");
        }
        FutureData<PushSubscription> future = new FutureData<PushSubscription>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(UPDATE));
        POST request = config.http().POST(uri, new PageReader(newRequestCallback(future, new PushSubscription())))
                .form("id", id);
        for (Map.Entry<String, String> e : connector.parameters().verifyAndGet().entrySet()) {
            request.form(e.getKey(), e.getValue());
        }
        if (name != null && !name.isEmpty()) {
            request.form("name", name);
        }
        applyConfig(request).execute();
        return future;
    }

    /**
     * Retrieve one or more interactions from a Push queue
     *
     * @param id     the push subscription ID
     * @param size   max number of interactions to get
     * @param cursor a pointer into the push
     * @return a set of interactions pulled from the specified push queue
     */
    public FutureData<PulledInteractions> pull(String id, int size, String cursor) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("A push subscription ID is required");
        }
        final FutureData<PulledInteractions> future = new FutureData<PulledInteractions>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(PULL));
        POST request = config.http().POST(uri, new PageReader(new Function2<String, Response>() {
            public void apply(String s, Response response) {
                if (!response.hasFailed()) {
                    JavaType type = DataSiftClient.MAPPER.getTypeFactory()
                            .constructCollectionType(List.class, Interaction.class);
                    try {
                        List<Interaction> interactions = DataSiftClient.MAPPER.readValue(s, type);
                        PulledInteractions pi = new PulledInteractions();
                        pi.data(interactions);
                        future.received(pi);
                    } catch (IOException e) {
                        fail(s, response, e);
                    }
                } else {
                    fail(s, response, null);
                }
            }

            private void fail(String s, Response response, Throwable e) {
                PulledInteractions pi = new PulledInteractions();
                pi.failed(e != null ? e : response.failureCause());
                pi.setResponse(new com.datasift.client.Response(s, response));
                future.received(pi);
            }
        })).form("id", id);
        if (cursor != null && !cursor.isEmpty()) {
            request.form("cursor", cursor);
        }
        if (size > 0) {
            request.form("size", size);
        }
        applyConfig(request).execute();
        return future;
    }

    public FutureData<PushLogMessages> log(String id, int page) {
        return log(id, page, 0, null, null);
    }

    /**
     * Retreive log messages about subscriptions
     *
     * @param id             the ID of a subscription
     * @param page           the page number
     * @param perPage        numbe rof items per page
     * @param orderBy        the field DataSift will use to order the result
     * @param orderDirection the direction of ordering, asc or desc
     * @return a set of log messages
     */
    public FutureData<PushLogMessages> log(String id, int page, int perPage, String orderBy, String orderDirection) {
        FutureData<PushLogMessages> future = new FutureData<PushLogMessages>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(LOG));
        POST request = config.http().POST(uri, new PageReader(newRequestCallback(future, new PushLogMessages())));
        if (id != null && !id.isEmpty()) {
            request.form("id", id);
        }
        if (page > 0) {
            request.form("page", page);
        }
        if (perPage > 0) {
            request.form("per_page", perPage);
        }
        if (orderBy != null && !orderBy.isEmpty()) {
            request.form("order_by", orderBy);
        }
        if (orderDirection != null && !orderDirection.isEmpty()) {
            request.form("order_dir", orderDirection);
        }
        applyConfig(request).execute();
        return future;
    }

    /**
     * @param id A push subscription ID
     * @return the push subscription for the given ID
     */
    public FutureData<PushSubscription> get(String id) {
        if (id == null) {
            throw new IllegalArgumentException("A subscription ID is required");
        }

        FutureData<PushSubscription> future = new FutureData<PushSubscription>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(GET));
        POST request = config.http().POST(uri, new PageReader(newRequestCallback(future, new PushSubscription())));
        applyConfig(request).execute();
        return future;
    }

    /**
     * Get all push subscriptions for the given stream
     *
     * @param hash            the ID of the stream to fetch all associated push subscriptions for
     * @param page            a page number
     * @param perPage         the amount of items per page
     * @param orderBy         the field name to order data by e.g. created_at
     * @param orderDirection  an order, asc or desc
     * @param includeFinished whether to included completed subscriptions or not
     * @return an {@link Iterable}
     */
    public FutureData<PushCollection> get(Stream hash, int page, int perPage, String orderBy,
                                          String orderDirection, boolean includeFinished) {
        FutureData<PushCollection> future = new FutureData<PushCollection>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(GET));
        POST request = config.http().POST(uri, new PageReader(newRequestCallback(future, new PushCollection())));
        request.form("hash", hash.hash()).form("include_finished", includeFinished ? 1 : 0);
        if (page > 0) {
            request.form("page", page);
        }
        if (perPage > 0) {
            request.form("per_page", perPage);
        }
        if (orderBy != null && !orderBy.isEmpty()) {
            request.form("order_by", orderBy);
        }
        if (orderDirection != null && !orderDirection.isEmpty()) {
            request.form("order_dir", orderDirection);
        }
        applyConfig(request).execute();
        return future;
    }

    /**
     * Get all push subscriptions for the given historics
     *
     * @param historics       the ID of the stream to fetch all associated push subscriptions for
     * @param page            a page number
     * @param perPage         the amount of items per page
     * @param orderBy         the field name to order data by e.g. created_at
     * @param orderDirection  an order, asc or desc
     * @param includeFinished whether to included completed subscriptions or not
     * @return an {@link Iterable}
     */
    public FutureData<PushCollection> get(HistoricsQuery historics, int page, int perPage, String orderBy,
                                          String orderDirection, boolean includeFinished) {
        FutureData<PushCollection> future = new FutureData<PushCollection>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(GET));
        POST request = config.http().POST(uri, new PageReader(newRequestCallback(future, new PushCollection())));
        request.form("playback_id", historics.getId()).form("include_finished", includeFinished ? 1 : 0);
        if (page > 0) {
            request.form("page", page);
        }
        if (perPage > 0) {
            request.form("per_page", perPage);
        }
        if (orderBy != null && !orderBy.isEmpty()) {
            request.form("order_by", orderBy);
        }
        if (orderDirection != null && !orderDirection.isEmpty()) {
            request.form("order_dir", orderDirection);
        }
        applyConfig(request).execute();
        return future;
    }

    /**
     * Check that the subscription details are correct
     *
     * @return the results of the validation
     */
    public <T extends PushConnector> FutureData<PushValidation> validate(OutputType<T> outputType, T connector) {
        FutureData<PushValidation> future = new FutureData<PushValidation>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(VALIDATE));
        POST request = config.http().POST(uri, new PageReader(newRequestCallback(future, new PushValidation())))
                .form("output_type", outputType.value());
        for (Map.Entry<String, String> e : connector.parameters().verifyAndGet().entrySet()) {
            request.form(e.getKey(), e.getValue());
        }
        applyConfig(request).execute();
        return future;
    }

    public <T extends PushConnector> FutureData<PushSubscription> create(final OutputType<T> outputType,
                                                                         final T connector,
                                                                         final FutureData<PreparedHistoricsQuery>
                                                                                 historics,
                                                                         FutureData<Stream> stream,
                                                                         final String name) {
        return create(outputType, connector, historics, stream, name, null, 0, 0);
    }

    /**
     * Create a push subscription from a prepared query
     *
     * @return the created subscription
     */
    public <T extends PushConnector> FutureData<PushSubscription> create(final OutputType<T> outputType,
                                                                         final T connector,
                                                                         PreparedHistoricsQuery historics,
                                                                         final String name,
                                                                         final String initialStatus,
                                                                         final long start,
                                                                         final long end) {
        return create(outputType, connector, FutureData.wrap(historics), null, name, initialStatus, start, end);
    }

    /**
     * Create a push subscription from a live stream
     *
     * @return the created subscription
     */
    public <T extends PushConnector> FutureData<PushSubscription> create(final OutputType<T> outputType,
                                                                         final T connector,
                                                                         Stream stream,
                                                                         final String name,
                                                                         final String initialStatus,
                                                                         final long start,
                                                                         final long end) {
        return create(outputType, connector, null, FutureData.wrap(stream), name, initialStatus, start, end);
    }

    /**
     * Creates a push subscription to either a historics query OR a live stream.
     * Of the parameters historics and stream, ONE and ONLY ONE must be null.
     * If the push subscription is to a a live stream the historics paramer MUST be null and if the subscription
     * is to a historics then the stream parameter MUST be null.
     *
     * @param outputType    One of the DataSift supported output types (connectors)
     * @param connector     a connector matching the one used in the first parameter
     * @param historics     a historics to which the subscription is being made or null if the subscription is to a
     *                      live stream
     * @param stream        A live stream to which this subscription is being created or null if the subscription is
     *                      to an historics query
     * @param name          a name for the subscription
     * @param initialStatus optionally one of {active,paused,waiting_for_start}
     * @param start         optional unix timestamp, ignored if < 1 - marks the start time for a subscription,
     *                      must be less than end
     * @param end           optional unix timestamp, ignored if < 1 - marks end time for a subscription,
     *                      must be greater than start
     * @param <T>
     * @return the subscription that will be created
     */
    public <T extends PushConnector> FutureData<PushSubscription> create(final OutputType<T> outputType,
                                                                         final T connector,
                                                                         FutureData<PreparedHistoricsQuery> historics,
                                                                         FutureData<Stream> stream,
                                                                         final String name,
                                                                         final String initialStatus,
                                                                         final long start,
                                                                         final long end) {
        if (name == null) {
            throw new IllegalArgumentException("Name is required in order to create a push subscription");
        }
        if (historics != null && stream != null) {
            throw new IllegalArgumentException("A push subscription cannot be created with both a historic and live " +
                    "stream. One must be null");
        }
        if (initialStatus != null
                && (!"active".equals(initialStatus)
                || !"paused".equals(initialStatus)
                || !"waiting_for_start".equals(initialStatus))) {
            throw new IllegalArgumentException(String.format("%s is an invalid initial status"));
        }
        if (end > 0 && !(end > start)) {
            throw new IllegalArgumentException("If end is specified it must be greater than the start");
        }
        final FutureData<PushSubscription> future = new FutureData<PushSubscription>();
        final PushSubscription subscription = new PushSubscription();
        //we need to unwrap the future data object, either historics or stream
        if (historics != null) {
            processFuture(historics, future, subscription, new FutureResponse<PreparedHistoricsQuery>() {
                public void apply(PreparedHistoricsQuery data) {
                    //using the unwrapped data, perform the actual query using historics as the source
                    performCreateQuery(outputType, connector, name, initialStatus, start, end, future, subscription,
                            data, null);
                }
            });
        }
        if (stream != null) {
            processFuture(stream, future, subscription, new FutureResponse<Stream>() {
                public void apply(Stream data) {
                    //using the unwrapped data, perform the actual query usihistoricsng a live stream as the source
                    performCreateQuery(outputType, connector, name, initialStatus, start, end, future, subscription,
                            null, data);
                }
            });
        }

        return future;
    }

    private <T extends PushConnector> void performCreateQuery(OutputType<T> outputType, T connector, String name,
                                                              String initialStatus, long start, long end,
                                                              FutureData<PushSubscription> future,
                                                              PushSubscription subscription,
                                                              PreparedHistoricsQuery historics,
                                                              Stream stream) {
        URI uri = newParams().forURL(config.newAPIEndpointURI(CREATE));
        POST request = config.http().POST(uri, new PageReader(newRequestCallback(future, subscription)))
                .form("output_type", outputType.value())
                .form("name", name);

        for (Map.Entry<String, String> e : connector.parameters().verifyAndGet().entrySet()) {
            request.form(e.getKey(), e.getValue());
        }

        if (historics != null) {
            request.form("playback_id", historics.getId());
        } else {
            request.form("hash", stream.hash());
        }
        if (initialStatus != null) {
            request.form("initial_status", initialStatus);
        }
        if (start > 0) {
            request.form("start", start);
        }
        if (end > 0) {
            request.form("end", end);
        }
        applyConfig(request).execute();
    }
}
