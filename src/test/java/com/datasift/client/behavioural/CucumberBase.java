package com.datasift.client.behavioural;

import com.datasift.client.DataSiftClient;
import com.datasift.client.DataSiftConfig;
import com.datasift.client.mock.MockServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.higgs.core.HiggsServer;
import org.junit.After;

public class CucumberBase {
    protected HiggsServer mock;
    CucumberMockWrapper wrapper = new CucumberMockWrapper();
    DataSiftConfig config = new DataSiftConfig("zcourts", "some-api-key") {
        {
            versionPrefix("behat-v1.3");
            host("localhost");
            setSslEnabled(false);
        }
    };
    DataSiftClient client = new DataSiftClient(config);
    ObjectMapper mapper = new ObjectMapper();

    public CucumberBase() {
        mock = MockServer.startNewServer();
        config.port(mock.getConfig().port);
        mock.registerClass(CucumberMockWrapper.class);
    }

    @After
    public void tearDown() throws Exception {
        MockServer.stop();
    }
}
