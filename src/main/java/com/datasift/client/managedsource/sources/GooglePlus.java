package com.datasift.client.managedsource.sources;

import com.datasift.client.DataSiftConfig;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class GooglePlus extends BaseSource<GooglePlus> {
    public GooglePlus(DataSiftConfig config) {
        setup(this, config);
    }

    /**
     * set to true, if you want to receive comment interactions.
     *
     * @return this
     */
    public GooglePlus enableComments(boolean enabled) {
        return setParam("comments", enabled);
    }

    /**
     * set to true, if you want to receive +1 interactions.
     *
     * @return this
     */
    public GooglePlus enablePlusOnes(boolean enabled) {
        return setParam("plus_ones", enabled);
    }
}
