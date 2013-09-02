package com.datasift.client.historics;

import com.datasift.client.ApiClient;
import com.datasift.client.DataSiftConfig;
import com.datasift.client.FutureData;
import io.higgs.http.client.POST;
import io.higgs.http.client.future.PageReader;

import java.net.URI;

/**
 * This class provides access to the DataSift Historics API.
 */
public class DataSiftHistorics extends ApiClient {
    public final String PREPARE = "historics/prepare", START = "historics/start", STOP = "historics/stop",
            UPDATE = "historics/update", STATUS = "historics/status", DELETE = "historics/delete",
            GET = "historics/get";

    public DataSiftHistorics(DataSiftConfig config) {
        super(config);
    }

    /**
     * @param hash    The hash of the CSDL for your playback query.
     *                Example values: 2459b03a13577579bca76471778a5c3d
     * @param start   Unix timestamp for the start time.
     *                Example values: 1325548800
     * @param end     nix timestamp for the end time. Must be at least 24 in the past.
     *                Example values: 1325548800
     * @param name    The name you assign to your playback query.
     *                Example values: Football
     * @param sources Comma-separated list of data sources to include. Currently,
     *                the only source you can use is twitter. In the future, you will be able to choose any source
     *                listed as a valid value that you would use in the interaction.type target.
     *                Example values: twitter
     * @return the prepared playback
     */
    public FutureData<HistoricsQuery> prepare(String hash, long start, long end, String name, String... sources) {
        return prepare(hash, start, end, name, -1, sources);
    }

    public FutureData<HistoricsQuery> prepare(String hash, long start, long end, String name, int sample,
                                              String... sources) {
        FutureData<HistoricsQuery> future = new FutureData<HistoricsQuery>();
        URI uri = newParams().forURL(config.newAPIEndpointURI(PREPARE));
        POST request = config.http().POST(uri, new PageReader(newRequestCallback(future, new HistoricsQuery())))
                .form("hash", hash)
                .form("start", start)
                .form("end", end)
                .form("name", name);
        if (sample > 0) {
            request.form("sample", sample);
        }
        if (sources != null && sources.length > 0) {
            StringBuilder b = new StringBuilder();
            for (String source : sources) {
                b.append(source).append(",");
            }
            request.form("sources", b.toString().substring(0, b.length() - 1));
        }
        applyConfig(request).execute();
        return future;
    }
}
