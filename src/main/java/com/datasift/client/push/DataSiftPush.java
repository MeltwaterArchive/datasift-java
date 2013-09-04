package com.datasift.client.push;

import com.datasift.client.ApiClient;
import com.datasift.client.DataSiftConfig;
import com.datasift.client.FutureData;
import com.datasift.client.FutureResponse;
import com.datasift.client.core.Stream;
import com.datasift.client.historics.HistoricsQuery;
import com.datasift.client.push.connectors.PushConnector;
import io.higgs.http.client.POST;
import io.higgs.http.client.future.PageReader;

import java.net.URI;
import java.util.Map;

/**
 * @author Courtney Robinson <courtney@crlog.info>
 */
public class DataSiftPush extends ApiClient {
    public final String VALIDATE = "push/validate", CREATE = "push/create", PAUSE = "push/pause",
            RESUME = "push/resume", UPDATE = "push/update", STOP = "push/stop", DELETE = "push/delete",
            LOG = "push/log", GET = "push/get", PULL = "push/pull";

    public DataSiftPush(DataSiftConfig config) {
        super(config);
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
                                                                         final FutureData<HistoricsQuery> historics,
                                                                         FutureData<Stream> stream,
                                                                         final String name) {
        return create(outputType, connector, historics, stream, name, null, 0, 0);
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
                                                                         final FutureData<HistoricsQuery> historics,
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
            processFuture(historics, future, subscription, new FutureResponse<HistoricsQuery>() {
                public void apply(HistoricsQuery data) {
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
                                                              PushSubscription subscription, HistoricsQuery historics,
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
