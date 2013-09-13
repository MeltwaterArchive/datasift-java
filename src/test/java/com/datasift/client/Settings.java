package com.datasift.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class Settings {
    /**
     * General timeout for any test which requires async/wait/notify
     */
    public static final long TIMEOUT = 30000;
    protected Logger log = LoggerFactory.getLogger(getClass());
    //strings, ints and boolean types are supported...nothing else
    private String username,
            apiKey,
            sampleStreamHash,
            urlEncodeWith;
    private int liveStreamCount;

    public String getUsername() {
        return username;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getSampleStreamHash() {
        return sampleStreamHash;
    }

    public String getUrlEncodeWith() {
        return urlEncodeWith;
    }

    public int getLiveStreamCount() {
        return liveStreamCount;
    }
}
