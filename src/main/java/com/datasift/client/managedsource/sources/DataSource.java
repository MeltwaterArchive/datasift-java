package com.datasift.client.managedsource.sources;

import com.datasift.client.managedsource.ManagedDataSourceType;

/*
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public interface DataSource<T extends DataSource<T>> {
    /*
     * @return A URL encoded string which represents the resources for this data source
     *         note this is the value for the "resources" field and does not include the key itself
     */
    String getResourcesAsJSON();

    /*
     * @return A URL encoded string which represents the auth information for this data source
     *         note this is the value for the "auth" field and does not include the key itself
     */
    String getAuthAsJSON();

    /*
     * @return A URL encoded string which represents the parameters for this data source
     *         note this is the value for the "parameters" field and does not include the key itself
     */
    String getParametersAsJSON();

    /*
     * @return a managed source type of the data source this represents
     */
    ManagedDataSourceType<T> type();

    /*
     * @return true if any auth information has been set for this data source
     */
    boolean hasAuth();

    /*
     * @return true if any resource parameters have been configured for this data source
     */
    boolean hasResources();

    /*
     * @return true if any "parameter" params have been configured for this data source
     */
    boolean hasParams();
}
