package com.datasift.client.managedsource;

import com.datasift.client.DataSiftApiClient;
import com.datasift.client.DataSiftConfig;
import com.datasift.client.managedsource.sources.DataSource;

/**
 * @author Courtney Robinson <courtney@crlog.info>
 */
public class DataSiftManagedSource extends DataSiftApiClient {
    public DataSiftManagedSource(DataSiftConfig config) {
        super(config);
    }

    public <T extends DataSource> void create(ManagedDataSourceType<T> sourceType, T source) {
        System.out.print("boo");
    }
}
