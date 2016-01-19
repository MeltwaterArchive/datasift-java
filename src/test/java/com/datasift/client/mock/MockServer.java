package com.datasift.client.mock;

import com.datasift.client.mock.datasift.MockCoreApi;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import io.higgs.core.HiggsServer;
import io.higgs.core.InvokableMethod;
import io.higgs.http.server.config.HttpConfig;
import io.higgs.http.server.protocol.HttpProtocolConfiguration;
import io.higgs.http.server.protocol.mediaTypeDecoders.JsonDecoder;

import java.net.URL;
import java.util.Queue;

import static org.junit.Assert.assertNotNull;

public class MockServer {
    private MockServer() {
    }

    protected static HttpProtocolConfiguration http;
    protected static HiggsServer server;
    protected static Queue<InvokableMethod> methods;
    protected static int instances;

    static {
        http = new HttpProtocolConfiguration();
        URL cfg = MockServer.class.getClassLoader().getResource("config.yml");
        assertNotNull(cfg);
        server = new HiggsServer() {
            {
                MockServer.methods = methods;
            }
        };
        server.setConfig(cfg.getPath(), HttpConfig.class);
        server.registerProtocol(http);
        //
        server.registerPackage(MockCoreApi.class.getPackage());
        server.start();

        JsonDecoder.mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, true);
        JsonDecoder.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JsonDecoder.mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        JsonDecoder.mapper.registerModule(new JodaModule());
    }

    public static Queue<InvokableMethod> methods() {
        return methods;
    }

    public static HiggsServer startNewServer() {
        instances++;
        server.getFactories().clear(); //clear previously registered factories
        //methods.clear(); //clear previously registered methods
        return server;
    }

    public static void stop() {
//        if (--instances <= 0) {
//            server.stop();
//        }
    }
}
