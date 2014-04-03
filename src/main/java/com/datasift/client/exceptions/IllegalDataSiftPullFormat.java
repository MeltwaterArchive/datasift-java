package com.datasift.client.exceptions;

import io.higgs.http.client.Response;

/**
 * @author Courtney Robinson <courtney@crlog.info>
 */
public class IllegalDataSiftPullFormat extends DataSiftException {
    public IllegalDataSiftPullFormat(String msg, Response response) {
        super(msg, null, response);
    }
}
