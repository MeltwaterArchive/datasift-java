package com.datasift.client.managedsource;

import com.datasift.client.DataSiftConfig;

/**
 * @author Courtney Robinson <courtney@crlog.info>
 */
public class DataSiftManagedSource {
    public DataSiftManagedSource(DataSiftConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("Config cannot be nulll");
        }

    }
}
