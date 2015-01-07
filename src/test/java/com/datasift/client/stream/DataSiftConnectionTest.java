package com.datasift.client.stream;

import com.datasift.client.DataSiftConfig;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataSiftConnectionTest {
    class ReconnectAttemptException extends RuntimeException {
    }

    public static class Conn extends DataSiftConnection {

        protected Conn(int maxAllowedSubscriptions, DataSiftConfig config, URI endpoint, ErrorListener errorListener,
                       StreamEventListener streamEventListener) {
            super(maxAllowedSubscriptions, config, endpoint, errorListener, streamEventListener);
        }
    }

    @Test(timeout = 3000, expected = ReconnectAttemptException.class)
    public void shouldReconnectOnTimeout() throws URISyntaxException, InterruptedException {
        DataSiftConfig config = new DataSiftConfig();
        //timeout after 1 second
        config.connectTimeout(1000);
        URI uri = new URI("wss://10.255.255.1");
        //connect to non-routable IP - http://stackoverflow.com/a/904609/400048
        Conn spy = mock(Conn.class); //spy(conn);
        when(spy.config()).thenReturn(config);
        when(spy.endpoint()).thenReturn(uri);
        when(spy.connect())
                .thenCallRealMethod() //call real method first time
                .thenThrow(ReconnectAttemptException.class); //throw error on reconnect attempt
        spy.connect();
    }
}
