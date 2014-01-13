package com.datasift.client.mock;

import com.datasift.client.mock.datasift.MockCoreApi;
import io.higgs.core.HiggsServer;
import io.higgs.http.server.config.HttpConfig;
import io.higgs.http.server.protocol.HttpProtocolConfiguration;

import java.net.URL;
import static org.junit.Assert.*;

public class MockServer {

    public static HiggsServer startNewServer() {
        HttpProtocolConfiguration http = new HttpProtocolConfiguration();
        URL cfg = MockServer.class.getClassLoader().getResource("config.yml");
        assertNotNull(cfg);
        HiggsServer server = new HiggsServer().setConfig(cfg.getPath(), HttpConfig.class);
        server.registerProtocol(http);
        //
        server.registerPackage(MockCoreApi.class.getPackage());
        server.start();
        return server;
    }
}
