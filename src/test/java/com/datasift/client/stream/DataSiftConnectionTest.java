package com.datasift.client.stream;

import com.datasift.client.DataSiftConfig;
import org.junit.Test;

import java.net.ConnectException;
import java.net.URI;

public class DataSiftConnectionTest {

    @Test(expected = ConnectException.class)
    public void shouldConnectOnConstruction() throws Exception {
        //connect on construction so connection should never be null
        new DataSiftConnection(1, new DataSiftConfig(), new URI("wss://127.0.0.1"), null, null);
    }
}
