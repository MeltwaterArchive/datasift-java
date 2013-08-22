package com.datasift.client.preview;

import com.datasift.client.DataSiftConfig;

/**
 * @author Courtney Robinson <courtney@crlog.info>
 */
public class DataSiftPreview {
    public DataSiftPreview(DataSiftConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("Config cannot be nulll");
        }
    }
}
