package com.datasift.client.managedsource.sources;

import com.datasift.client.DataSiftConfig;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class Instagram extends BaseSource<Instagram> {
    public Instagram(DataSiftConfig config) {
        setup(this, config);
    }
}
