package com.datasift.client.mock.datasift;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.HashMap;
import java.util.Map;

@Path("/")
public class MockODPApi {

    Map<String, String> headers = new HashMap<>();
    private long accepted;
    private long totalMessageBytes;

    @POST
    @Path("testsource")
    public Map<String, Object> batch() {
        Map<String, Object> map = new HashMap<>();
        map.put("accepted", accepted);
        map.put("total_message_bytes", totalMessageBytes);

        return map;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setAccepted(long accepted) {
        this.accepted = accepted;
    }

    public void setTotalMessageBytes(long totalMessageBytes) {
        this.totalMessageBytes = totalMessageBytes;
    }
}
