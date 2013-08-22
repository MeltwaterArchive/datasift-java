package com.datasift.client;

import com.datasift.client.core.DataSiftCore;
import com.datasift.client.historics.DataSiftHistorics;
import com.datasift.client.managedsource.DataSiftManagedSource;
import com.datasift.client.preview.DataSiftPreview;
import com.datasift.client.push.DataSiftPush;

/**
 * This class is the gateway to the DataSift client APIs. It provides an easy to use,
 * configurable interface for accessing all DataSift features
 */
public class DataSiftClient {
    protected DataSiftConfig config;
    protected DataSiftHistorics historics;
    private DataSiftManagedSource source;
    private DataSiftCore core;
    private DataSiftPreview preview;
    private DataSiftPush push;

    public DataSiftClient() {
        this(new DataSiftConfig());
    }

    /**
     * @param config a configuration which should be used for making API requests
     */
    public DataSiftClient(DataSiftConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("DataSift config cannot be null");
        }
        this.config = config;
        this.historics = new DataSiftHistorics(config);
        this.source = new DataSiftManagedSource(config);
        this.core = new DataSiftCore(config);
        this.preview = new DataSiftPreview(config);
        this.push = new DataSiftPush(config);
    }

    /**
     * @return An object suitable for making requests to the DataSift Historics API
     */
    public DataSiftHistorics historics() {
        return historics;
    }

    /**
     * @return An object suitable for making requests to the DataSift Managed sources API
     */
    public DataSiftManagedSource managedSource() {
        return source;
    }

    /**
     * @return An object suitable for making requests to the DataSift Core API
     */
    public DataSiftCore core() {
        return core;
    }

    /**
     * @return An object suitable for making requests to the DataSift Preview API
     */
    public DataSiftPreview preview() {
        return preview;
    }

    /**
     * @return An object suitable for making requests to the DataSift Push API
     */
    public DataSiftPush push() {
        return push;
    }

    /**
     * @return the instance of the configuration being used for all API calls made through this client
     */
    public DataSiftConfig config() {
        return config;
    }
}
