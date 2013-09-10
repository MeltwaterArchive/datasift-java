package com.datasift.client.managedsource.sources;

import com.datasift.client.DataSiftConfig;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class FacebookPage extends BaseSource<FacebookPage> {

    public FacebookPage(DataSiftConfig config) {
        setup(this, config);
    }

    /**
     * set to true, if you want to receive likes interactions.
     *
     * @return this
     */
    public FacebookPage enableLikes(boolean enabled) {
        return setParam("likes", enabled);
    }

    /**
     * set to true, if you want to receive interactions related to posts created on the monitored page by the users
     * who interact with that page, but do not administer it.
     *
     * @return this
     */
    public FacebookPage enablePostsByOthers(boolean enabled) {
        return setParam("posts_by_others", enabled);
    }

    /**
     * set to true, if you want to receive interactions that carry comments related to the posts published on the
     * monitored page.
     *
     * @return this
     */
    public FacebookPage enableComments(boolean enabled) {
        return setParam("comments", enabled);
    }
}
