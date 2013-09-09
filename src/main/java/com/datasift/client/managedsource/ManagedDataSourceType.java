package com.datasift.client.managedsource;

import com.datasift.client.managedsource.sources.DataSource;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class ManagedDataSourceType<T extends DataSource> {
    public static ManagedDataSourceType FACEBOOK_PAGE = new ManagedDataSourceType("facebook_page");
    public static ManagedDataSourceType GOOGLE_PLUS = new ManagedDataSourceType("googleplus");
    public static ManagedDataSourceType INSTAGRAM = new ManagedDataSourceType("instagram");
    public static ManagedDataSourceType YAMMER = new ManagedDataSourceType("yammer");
    private final String value;

    public ManagedDataSourceType(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("A value is required");
        }
        this.value = value;
    }

    public String value() {
        return value;
    }
}
