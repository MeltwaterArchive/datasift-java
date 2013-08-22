package com.datasift.client.push;

import com.datasift.client.DataSiftConfig;

/**
 * @author Courtney Robinson <courtney@crlog.info>
 */
public class DataSiftPush {
    public DataSiftPush(DataSiftConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("Config cannot be nulll");
        }
    }
}
