package com.datasift.client.preview;

import com.datasift.client.DataSiftApiClient;
import com.datasift.client.DataSiftConfig;
import com.datasift.client.FutureData;
import com.datasift.client.core.Stream;
import io.higgs.http.client.POST;
import io.higgs.http.client.readers.PageReader;
import org.joda.time.DateTime;

import java.net.URI;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * @author Courtney Robinson <courtney@crlog.info>
 */
public class DataSiftPreview extends DataSiftApiClient {
    public final String CREATE = "preview/create", GET = "preview/get";

    public DataSiftPreview(DataSiftConfig config) {
        super(config);
    }

    public FutureData<HistoricsPreview> create(DateTime now, Stream stream, String[] params) {
        return create(now, null, stream, params);
    }

    public FutureData<HistoricsPreview> create(DateTime start, DateTime end, Stream hash, String[] params) {
        if (start == null || hash == null || params == null) {
            throw new IllegalArgumentException("A valid params, hash, start time are required");
        }
        return create(MILLISECONDS.toSeconds(start.getMillis()),
                end == null ? 0 : MILLISECONDS.toSeconds(end.getMillis()), hash, params);
    }

    public FutureData<HistoricsPreview> create(long start, long end, String hash, String[] parameters) {
        if (hash == null || parameters == null) {
            throw new IllegalArgumentException("Valid params and hash are required");
        }
        return create(start, end, Stream.fromString(hash), parameters);
    }

    /**
     * Create a historic preview for the given stream within the given time frame, using the set of parameters provided
     *
     * @param start      a timestamp of when to start the preview from
     * @param end        optionally when the preview ends -  If not specified, i.e. set to a value less than 1,
     *                   defaults to the earliest out of start + 24 hours or now - 1 hour.
     * @param stream     the stream/filter to create the preview for
     * @param parameters A list of at least one but no more than 20 Historics Preview parameters e.g.  target,
     *                   analysis,argument  see http://dev.datasift.com/docs/api/1/previewcreate for documentation of
     *                   available parameters
     * @return the preview created
     */
    public FutureData<HistoricsPreview> create(long start, long end, Stream stream, String[] parameters) {
        if (stream == null) {
            throw new IllegalArgumentException("A valid hash is required");
        }
        if (parameters == null || parameters.length == 0) {
            throw new IllegalArgumentException("A at least 1 historics preview parameter is required");
        }
        if (parameters.length > 20) {
            throw new IllegalArgumentException("No more than 20 historics preview parameters are allowed");
        }
        FutureData<HistoricsPreview> future = new FutureData<HistoricsPreview>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(CREATE));
        POST request = config.http()
                .POST(uri, new PageReader(newRequestCallback(future, new HistoricsPreview(), config)))
                .form("start", start)
                .form("hash", stream.hash());
        StringBuilder b = new StringBuilder();
        for (String p : parameters) {
            b.append(p).append(',');
        }
        request.form("parameters", b.toString().substring(0, b.length() - 1));
        if (end > 0) {
            request.form("end", end);
        }
        applyConfig(request).execute();
        return future;
    }

    public FutureData<HistoricsPreviewData> get(String id) {
        return get(HistoricsPreview.fromString(id));
    }

    /**
     * Get the data that's available for the given preview
     *
     * @param preview the historics preview to fetch
     * @return the data available
     */
    public FutureData<HistoricsPreviewData> get(HistoricsPreview preview) {
        if (preview == null || preview.id() == null) {
            throw new IllegalArgumentException("A valid preview isntance is required");
        }
        FutureData<HistoricsPreviewData> future = new FutureData<HistoricsPreviewData>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(GET));
        POST request = config.http()
                .POST(uri, new PageReader(newRequestCallback(future, new HistoricsPreviewData(), config)))
                .form("id", preview.id());
        applyConfig(request).execute();
        return future;
    }
}
