package com.datasift.client.push.connectors;

import com.datasift.client.push.OutputType;

/*
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public interface PushConnector<T extends PushConnector<T>> {
    /*
     * Every push connector has a set of output parameters used to configure the destination.
     * It is expected that this method will return a map of the form
     * <p/>
     * output_params.variable-name ==> value
     * <p/>
     * Each connector implementation should* do it's own verification of the map's contents
     */
    Prepared parameters();

    OutputType<T> type();
}
