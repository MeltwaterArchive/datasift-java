package com.datasift.client.managedsource.sources;

import com.datasift.client.DataSiftConfig;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class Instagram extends BaseSource<Instagram> {
    public Instagram(DataSiftConfig config) {
        setup(this, config);
    }

    /**
     * set to true, if you want to receive like interactions.
     *
     * @return this
     */
    public Instagram enableLikes(boolean enabled) {
        return setParam("likes", enabled);
    }

    /**
     * set to true, if you want to receive comment interactions.
     *
     * @return this
     */
    public Instagram enableComments(boolean enabled) {
        return setParam("comments", enabled);
    }

    public Instagram id(String url) {
        return setParam("id", url);
    }

}
