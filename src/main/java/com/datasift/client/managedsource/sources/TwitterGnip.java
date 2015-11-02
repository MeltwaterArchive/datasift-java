package com.datasift.client.managedsource.sources;

import com.datasift.client.DataSiftConfig;
import com.datasift.client.managedsource.ManagedDataSourceType;

public class TwitterGnip extends BaseSource<TwitterGnip> {

    public TwitterGnip(DataSiftConfig config) {
        super(config, ManagedDataSourceType.TWITTER_GNIP);
        // set the gnip specific ingestion mapping for data sent to ODP
        setParametersField("mapping", "gnip_1");
    }

    public TwitterGnip setMapping(String mapping) {
        if (mapping == null) {
            throw new IllegalArgumentException("Mapping required");
        }
        return setParametersField("mapping", mapping);
    }
}
