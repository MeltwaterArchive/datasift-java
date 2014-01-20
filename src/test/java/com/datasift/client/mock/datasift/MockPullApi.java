package com.datasift.client.mock.datasift;

import com.datasift.client.Response;
import com.datasift.client.core.Stream;
import com.datasift.client.push.PushLogMessages;
import com.datasift.client.push.Status;
import com.datasift.client.push.connectors.PushConnector;
import com.datasift.client.stream.Interaction;
import io.higgs.core.method;
import io.higgs.http.server.params.FormParams;
import io.higgs.http.server.resource.POST;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by agnieszka on 15/01/2014.
 */

@method("/v1.1/pull")
public class MockPullApi extends MockPushApi{


    @method
    public Map<String, Object> pull(FormParams params) {
        for(Map.Entry<String, String> e : s3Params.entrySet()){
            Object expected = params.get(e.getKey());
            assertNotNull(expected);
            assertEquals(expected,e.getValue());
        }
        Map<String, Object> map = new HashMap<>();
        map.put("interactions", interactions);
        return map;
    }
}
