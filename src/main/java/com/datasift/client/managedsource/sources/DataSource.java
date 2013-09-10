package com.datasift.client.managedsource.sources;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public interface DataSource {
    String getURLEncodedResources();

    String getURLEncodedAuth();

    String getURLEncodedParameters();
}
