package com.datasift.client.mock.datasift;

import com.datasift.client.managedsource.ManagedSource;
import com.datasift.client.managedsource.ManagedSourceLog;
import com.datasift.client.managedsource.sources.DataSource;
import com.datasift.client.managedsource.sources.FacebookPage;
import io.higgs.core.method;
import io.higgs.http.server.HttpResponse;
import io.higgs.http.server.params.FormParam;
import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Created by agnieszka on 17/01/2014.
 */
@method("/v1.1/source")
public class MockManagedSourcesApi {
    Map<String, String> headers = new HashMap<>();
    private Map<String, Object> streams = new HashMap<>();
    private MockManagedSourcesApi m = new MockManagedSourcesApi();
    private String name = "";
    private String id;
    private ManagedSource m_id;
    private String source_type = "";
    private Map<String, Object> parameters;
    private Set<ManagedSource.AuthParams> auth;
    private Set<ManagedSource.ResourceParams> resource;
    private DateTime created_at = DateTime.now();
    private int count = new Random().nextInt();
    private int page = new Random().nextInt();
    private int pages = new Random().nextInt();
    private int per_page = new Random().nextInt();
    private List<ManagedSourceLog.LogMessage> entries;

    @method("create")
    public Map<String, Object> create() {
        Map<String, Object> map = new HashMap<>();

        return map;
    }

    @method("update")
    public Map<String, Object> update() {
        Map<String, Object> map = new HashMap<>();

        return map;
    }

    @method("delete")
    public Map<String, Object> delete() {
        Map<String, Object> map = new HashMap<>();

        return map;
    }

    @method("log")
    public Map<String, Object> log() {
        Map<String, Object> map = new HashMap<>();

        return map;
    }

    @method("get")
    public Map<String, Object> get() {
        Map<String, Object> map = new HashMap<>();

        return map;
    }

    @method("stop")
    public Map<String, Object> stop() {
        Map<String, Object> map = new HashMap<>();

        return map;
    }

    @method("start")
    public Map<String, Object> start() {
        Map<String, Object> map = new HashMap<>();

        return map;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

}
