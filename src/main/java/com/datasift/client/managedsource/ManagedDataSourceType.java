package com.datasift.client.managedsource;

import com.datasift.client.managedsource.sources.DataSource;
import com.datasift.client.managedsource.sources.FacebookPage;
import com.datasift.client.managedsource.sources.GooglePlus;
import com.datasift.client.managedsource.sources.Instagram;
import com.datasift.client.managedsource.sources.TwitterGnip;
import com.datasift.client.managedsource.sources.Yammer;

/*
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class ManagedDataSourceType<T extends DataSource> {

    public static final ManagedDataSourceType<FacebookPage> FACEBOOK_PAGE =
            new ManagedDataSourceType<FacebookPage>("facebook_page");
    public static final ManagedDataSourceType<GooglePlus> GOOGLE_PLUS =
            new ManagedDataSourceType<GooglePlus>("googleplus");
    public static final ManagedDataSourceType<Instagram> INSTAGRAM =
            new ManagedDataSourceType<Instagram>("instagram");
    public static final ManagedDataSourceType<Yammer> YAMMER =
            new ManagedDataSourceType<Yammer>("yammer");
    public static final ManagedDataSourceType<TwitterGnip> TWITTER_GNIP =
            new ManagedDataSourceType<TwitterGnip>("twitter_gnip");
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
