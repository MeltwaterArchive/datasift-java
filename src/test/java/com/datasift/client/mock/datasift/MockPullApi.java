package com.datasift.client.mock.datasift;

import io.higgs.core.method;
import io.higgs.http.server.params.FormParams;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by agnieszka on 15/01/2014.
 */

@method("/v1.1/pull")
public class MockPullApi extends MockPushApi {

    @method
    public Map<String, Object> pull(FormParams params) {
        for (Map.Entry<String, String> e : s3Params.entrySet()) {
            Object expected = params.get(e.getKey());
            assertNotNull(expected);
            assertEquals(expected, e.getValue());
        }
        Map<String, Object> map = new HashMap<>();
        map.put("interactions", interactions);
        return map;
    }
}
