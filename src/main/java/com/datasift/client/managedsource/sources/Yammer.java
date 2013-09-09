package com.datasift.client.managedsource.sources;

import com.datasift.client.DataSiftConfig;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class Yammer extends BaseSource<Yammer> {
    public Yammer(DataSiftConfig config) {
        setup(this, config);
    }
}
