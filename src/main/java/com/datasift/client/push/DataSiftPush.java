package com.datasift.client.push;

import com.datasift.client.BaseDataSiftResult;
import com.datasift.client.DataSiftApiClient;
import com.datasift.client.DataSiftConfig;
import com.datasift.client.DataSiftResult;
import com.datasift.client.FutureData;
import com.datasift.client.FutureResponse;
import com.datasift.client.core.Stream;
import com.datasift.client.exceptions.AuthException;
import com.datasift.client.exceptions.DataSiftException;
import com.datasift.client.historics.HistoricsQuery;
import com.datasift.client.historics.PreparedHistoricsQuery;
import com.datasift.client.push.connectors.PushConnector;
import com.datasift.client.push.pull.LastInteraction;
import com.datasift.client.push.pull.PullJsonType;
import io.higgs.http.client.HttpRequestBuilder;
import io.higgs.http.client.POST;
import io.higgs.http.client.readers.PageReader;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.joda.time.DateTime;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Courtney Robinson <courtney@crlog.info>
 */
public class DataSiftPush extends DataSiftApiClient {
    public final String VALIDATE = "push/validate", CREATE = "push/create", PAUSE = "push/pause",
            RESUME = "push/resume", UPDATE = "push/update", STOP = "push/stop", DELETE = "push/delete",
            LOG = "push/log", GET = "push/get", PULL = "pull";

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
        FutureData<PushSubscription> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(PAUSE));
        POST request = config.http()
                .POST(uri, new PageReader(newRequestCallback(future, new PushSubscription(), config)))
                .form("id", id);
        performRequest(future, request);
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
        FutureData<PushSubscription> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(RESUME));
        POST request = config.http()
                .POST(uri, new PageReader(newRequestCallback(future, new PushSubscription(), config)))
                .form("id", id);
        performRequest(future, request);
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
        FutureData<PushSubscription> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(STOP));
        POST request = config.http()
                .POST(uri, new PageReader(newRequestCallback(future, new PushSubscription(), config)))
                .form("id", id);
        performRequest(future, request);
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
        FutureData<DataSiftResult> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(DELETE));
        POST request = config.http()
                .POST(uri, new PageReader(newRequestCallback(future, new BaseDataSiftResult(), config)))
                .form("id", id);
        performRequest(future, request);
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
        FutureData<PushSubscription> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(UPDATE));
        POST request = config.http()
                .POST(uri, new PageReader(newRequestCallback(future, new PushSubscription(), config)))
                .form("id", id);
        for (Map.Entry<String, String> e : connector.parameters().verifyAndGet().entrySet()) {
            request.form(e.getKey(), e.getValue());
        }
        if (name != null && !name.isEmpty()) {
            request.form("name", name);
        }
        performRequest(future, request);
        return future;
    }

    public FutureData<PulledInteractions> pull(PushSubscription id) {
        return pull(id, 0, null);
    }

    /**
     * Retrieve one or more interactions from a Push queue
     *
     * @param id     the push subscription ID
     * @param size   max number of interactions to list
     * @param cursor a pointer into the push
     * @return a set of interactions pulled from the specified push queue
     */
    public FutureData<PulledInteractions> pull(final PushSubscription id, final int size, final String cursor) {
        if (id == null || id.getId() == null || id.getId().isEmpty()) {
            throw new IllegalArgumentException("A push subscription ID is required");
        }
        final FutureData<PulledInteractions> future = new FutureData<>();
        final URI uri = newParams().forURL(config.newAPIEndpointURI(PULL));
        final PulledInteractions interactions = new PulledInteractions();
        final PullReader reader = new PullReader(interactions) {
            @Override
            public void onStatus(HttpResponseStatus status) {
                super.onStatus(status);
                if (response.hasFailed()) {
                    throw new DataSiftException("Failed to pull interactions", response.failureCause());
                }
                if (status.code() == 401) {
                    throw new AuthException("Please provide a valid username and API key", response);
                }
            }

            @Override
            public void done() {
                super.done();
                final PullReader internalReader = this;
                if (status == 204 && successiveNoContent >= 5) {
                    //if we've tried to fetch and gotten no content 5 times successively then it's time to see if
                    //the subscription is finishing/finished or waiting to start
                    PushSubscription subscription = get(id.getId()).sync();
                    if (subscription.status() != null) {
                        Status s = subscription.status();
                        if (s.isWaitingForStart()) {
                            //if the subscription hasn't started yet then figure out when it should start and
                            //set a back off of that many seconds
                            backOff = new Long(subscription.getStart() -
                                    TimeUnit.MILLISECONDS.toSeconds(DateTime.now().getMillis())).intValue();
                        } else if (s.isFailed() || s.isFinished()) {
                            //cause client to know we've stopped without calling get themselves
                            interactions.add(LastInteraction.INSTANCE);
                            interactions.stopPulling();
                        }
                    }
                }
                if ((status == 204 && interactions.isPulling()) ||
                        interactions.isPulling() && nextCursor != null && !nextCursor.isEmpty()) {
                    HttpRequestBuilder.group().schedule(new Runnable() {
                        @Override
                        public void run() {
                            sendPullRequest(future, id, size, nextCursor, uri, internalReader);
                        }
                        //wait for at least the back off period,
                        //if we're not backing off this is 0 so it runs immediately
                    }, backOff, TimeUnit.SECONDS);
                }
                //reader is re-used so reset states
                reset();
            }
        };
        sendPullRequest(future, id, size, cursor, uri, reader);
        //unlike other response types we can provide the object before the request completes
        future.received(interactions);
        return future;
    }

    protected void sendPullRequest(FutureData<PulledInteractions> future, PushSubscription id, int size, String cursor,
                                   URI uri, PullReader reader) {
        POST request = config.http().POST(uri, reader).form("id", id.getId());
        if (cursor != null && !cursor.isEmpty()) {
            request.form("cursor", cursor);
        }
        if (size > 0) {
            request.form("size", size);
        }
        performRequest(future, request);
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
        FutureData<PushLogMessages> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(LOG));
        POST request = config.http()
                .POST(uri, new PageReader(newRequestCallback(future, new PushLogMessages(), config)));
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
        performRequest(future, request);
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

        FutureData<PushSubscription> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(GET));
        POST request = config.http()
                .POST(uri, new PageReader(newRequestCallback(future, new PushSubscription(), config)));
        request.form("id", id);
        performRequest(future, request);
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
        FutureData<PushCollection> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(GET));
        POST request = config.http()
                .POST(uri, new PageReader(newRequestCallback(future, new PushCollection(), config)));
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
        performRequest(future, request);
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
        FutureData<PushCollection> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(GET));
        POST request = config.http()
                .POST(uri, new PageReader(newRequestCallback(future, new PushCollection(), config)));
        request.form("historics_id", historics.getId()).form("include_finished", includeFinished ? 1 : 0);
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
        performRequest(future, request);
        return future;
    }

    /**
     * Check that the subscription details are correct
     *
     * @return the results of the validation
     */
    public <T extends PushConnector> FutureData<PushValidation> validate(T connector) {
        FutureData<PushValidation> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(VALIDATE));
        POST request = config.http().POST(uri, new PageReader(newRequestCallback(future, new PushValidation(), config)))
                .form("output_type", connector.type().value());
        for (Map.Entry<String, String> e : connector.parameters().verifyAndGet().entrySet()) {
            request.form(e.getKey(), e.getValue());
        }
        performRequest(future, request);
        return future;
    }

    public <T extends PushConnector> FutureData<PushSubscription> create(final T connector,
                                                                         final FutureData<PreparedHistoricsQuery>
                                                                                 historics,
                                                                         FutureData<Stream> stream,
                                                                         final String name) {
        return create(connector, historics, stream, name, null, 0, 0);
    }

    /**
     * Create a push subscription from a prepared query
     *
     * @return the created subscription
     */
    public <T extends PushConnector> FutureData<PushSubscription> create(final T connector,
                                                                         PreparedHistoricsQuery historics,
                                                                         final String name,
                                                                         final Status initialStatus,
                                                                         final long start,
                                                                         final long end) {
        return create(connector, FutureData.wrap(historics), null, name, initialStatus, start, end);
    }

    public <T extends PushConnector> FutureData<PushSubscription> create(T con,
                                                                         FutureData<PreparedHistoricsQuery> query,
                                                                         String name) {
        return create(con, query, null, name);
    }

    public <T extends PushConnector> FutureData<PushSubscription> create(final T con, Stream stream, String name) {
        return create(con, stream, name, null, 0, 0);
    }

    /**
     * Create a push subscription from a live stream
     *
     * @return the created subscription
     */
    public <T extends PushConnector> FutureData<PushSubscription> create(final T connector,
                                                                         Stream stream,
                                                                         final String name,
                                                                         final Status initialStatus,
                                                                         final long start,
                                                                         final long end) {
        return create(connector, null, FutureData.wrap(stream), name, initialStatus, start, end);
    }

    /**
     * Create a push subscription to be consumed via {@link #pull(PushSubscription, int, String)} using a live stream
     *
     * @param historics     the historic query which will be consumed via pull
     * @param name          a name for the subscription
     * @param initialStatus the initial status of the subscription
     * @param start         an option timestamp of when to start the subscription
     * @param end           an optional timestamp of when to stop
     * @return this
     */
    public FutureData<PushSubscription> createPull(PullJsonType jsonMeta, PreparedHistoricsQuery historics, String name,
                                                   Status initialStatus, long start, long end) {
        return createPull(jsonMeta, historics, null, name, initialStatus, start, end);
    }

    public FutureData<PushSubscription> createPull(PullJsonType jsonMeta, PreparedHistoricsQuery historics,
                                                   String name) {
        return createPull(jsonMeta, historics, null, name, null, 0, 0);
    }

    /**
     * Create a push subscription to be consumed via {@link #pull(PushSubscription, int, String)} using a live stream
     *
     * @param stream        the stream which will be consumed via pull
     * @param name          a name for the subscription
     * @param initialStatus the initial status of the subscription
     * @param start         an option timestamp of when to start the subscription
     * @param end           an optional timestamp of when to stop
     * @return this
     */
    public FutureData<PushSubscription> createPull(PullJsonType jsonMeta, Stream stream, String name,
                                                   Status initialStatus, long start, long end) {
        return createPull(jsonMeta, null, stream, name, initialStatus, start, end);
    }

    public FutureData<PushSubscription> createPull(PullJsonType jsonMeta, Stream stream, String name) {
        return createPull(jsonMeta, null, stream, name, null, 0, 0);
    }

    public FutureData<PushSubscription> createPull(PullJsonType type, PreparedHistoricsQuery historics, Stream stream,
                                                   String name,
                                                   Status initialStatus,
                                                   long start,
                                                   long end) {
        final FutureData<PushSubscription> future = new FutureData<>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(CREATE));
        POST request = config.http()
                .POST(uri, new PageReader(newRequestCallback(future, new PushSubscription(), config)))
                .form("output_type", "pull")
                .form("output_params.acl", "private")
                .form("name", name);
        if (type != null) {
            request.form("output_params.format", type.asString());
        }
        if (historics != null && stream != null) {
            throw new IllegalStateException("Historics and Stream cannot both be specified");
        }
        if (historics == null && stream == null) {
            throw new IllegalArgumentException("At least one of Historics OR Stream must be specified");
        }

        if (historics != null) {
            request.form("historics_id", historics.getId());
        }
        if (stream != null) {
            request.form("hash", stream.hash());
        }
        if (initialStatus != null) {
            request.form("initial_status", initialStatus.val());
        }
        if (start > 0) {
            request.form("start", start);
        }
        if (end > 0) {
            request.form("end", end);
        }
        performRequest(future, request);
        return future;
    }

    /**
     * Creates a push subscription to either a historics query OR a live stream.
     * Of the parameters historics and stream, ONE and ONLY ONE must be null.
     * If the push subscription is to a a live stream the historics paramer MUST be null and if the subscription
     * is to a historics then the stream parameter MUST be null.
     *
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
     * @return the subscription that will be created
     */
    public <T extends PushConnector> FutureData<PushSubscription> create(final T connector,
                                                                         FutureData<PreparedHistoricsQuery>
                                                                                 historics, FutureData<Stream> stream,
                                                                         final String name,
                                                                         final Status initialStatus,
                                                                         final long start,
                                                                         final long end) {
        if (name == null) {
            throw new IllegalArgumentException("Name is required in order to create a push subscription");
        }
        if (historics != null && stream != null) {
            throw new IllegalArgumentException("A push subscription cannot be created with both a historic and live " +
                    "stream. One must be null");
        }
        if (end > 0 && !(end > start)) {
            throw new IllegalArgumentException("If end is specified it must be greater than the start");
        }
        final FutureData<PushSubscription> future = new FutureData<>();
        final PushSubscription subscription = new PushSubscription();
        //we need to unwrap the future data object, either historics or stream
        if (historics != null) {
            unwrapFuture(historics, future, subscription, new FutureResponse<PreparedHistoricsQuery>() {
                public void apply(PreparedHistoricsQuery data) {
                    //using the unwrapped data, perform the actual query using historics as the source
                    performCreateQuery(connector, name, initialStatus, start, end, future, subscription,
                            data, null);
                }
            });
        }
        if (stream != null) {
            unwrapFuture(stream, future, subscription, new FutureResponse<Stream>() {
                public void apply(Stream data) {
                    //using the unwrapped data, perform the actual query usihistoricsng a live stream as the source
                    performCreateQuery(connector, name, initialStatus, start, end, future, subscription,
                            null, data);
                }
            });
        }

        return future;
    }

    private <T extends PushConnector> void performCreateQuery(T connector, String name,
                                                              Status initialStatus, long start, long end,
                                                              FutureData<PushSubscription> future,
                                                              PushSubscription subscription,
                                                              PreparedHistoricsQuery historics,
                                                              Stream stream) {
        URI uri = newParams().forURL(config.newAPIEndpointURI(CREATE));
        POST request = config.http().POST(uri, new PageReader(newRequestCallback(future, subscription, config)))
                .form("output_type", connector.type().value())
                .form("name", name);

        for (Map.Entry<String, String> e : connector.parameters().verifyAndGet().entrySet()) {
            request.form(e.getKey(), e.getValue());
        }

        if (historics != null) {
            request.form("historics_id", historics.getId());
        } else {
            request.form("hash", stream.hash());
        }
        if (initialStatus != null) {
            request.form("initial_status", initialStatus.val());
        }
        if (start > 0) {
            request.form("start", start);
        }
        if (end > 0) {
            request.form("end", end);
        }
        performRequest(future, request);
    }
}
