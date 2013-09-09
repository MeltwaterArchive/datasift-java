package com.datasift.client.managedsource.sources;

import com.datasift.client.DataSiftConfig;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class FacebookPage extends BaseSource<FacebookPage> {
    public FacebookPage(DataSiftConfig config) {
        setup(this, config);
    }
}
