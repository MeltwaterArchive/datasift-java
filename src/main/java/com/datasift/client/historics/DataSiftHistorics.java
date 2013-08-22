package com.datasift.client.historics;

import com.datasift.client.DataSiftConfig;

/**
 * This class provides access to the DataSift Historics API.
 */
public class DataSiftHistorics {
    private DataSiftConfig config;

    public DataSiftHistorics(DataSiftConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("Config cannot be nulll");
        }
        this.config = config;
    }
}
