package com.datasift.client.managedsource.sources;

import com.datasift.client.DataSiftConfig;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class GooglePlus extends BaseSource<GooglePlus> {
    public GooglePlus(DataSiftConfig config) {
        setup(this, config);
    }
}
