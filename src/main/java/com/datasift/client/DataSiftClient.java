package com.datasift.client;

import com.datasift.client.core.DataSiftCore;
import com.datasift.client.historics.DataSiftHistorics;
import com.datasift.client.managedsource.DataSiftManagedSource;
import com.datasift.client.preview.DataSiftPreview;
import com.datasift.client.push.DataSiftPush;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import io.higgs.http.client.HttpRequestBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * This class is the gateway to the DataSift client APIs. It provides an easy to use,
 * configurable interface for accessing all DataSift features
 */
public class DataSiftClient {
    public static final ObjectMapper MAPPER = new ObjectMapper();
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * Where ever a numeric value is optional and it happens to be absent in a response this value will be used
     */
    public static final int DEFAULT_NUM = Integer.MIN_VALUE;
    protected DataSiftConfig config;
    protected DataSiftHistorics historics;
    protected DataSiftManagedSource source;
    protected DataSiftCore core;
    protected DataSiftPreview preview;
    protected DataSiftPush push;

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
        configureMapper();
        this.config = config;
        this.historics = new DataSiftHistorics(config);
        this.source = new DataSiftManagedSource(config);
        this.core = new DataSiftCore(config);
        this.preview = new DataSiftPreview(config);
        this.push = new DataSiftPush(config);
    }

    protected void configureMapper() {
        MAPPER.setDateFormat(new SimpleDateFormat(DATE_FORMAT));
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        MAPPER.registerModule(new JodaModule());
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

    public void shutdown() {
        HttpRequestBuilder.shutdown();
    }
}
