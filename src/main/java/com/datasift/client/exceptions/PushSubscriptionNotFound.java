package com.datasift.client.exceptions;

import io.higgs.http.client.Response;

/**
 * @author Courtney Robinson <courtney@crlog.info>
 */
public class PushSubscriptionNotFound extends DataSiftException {
    public PushSubscriptionNotFound(Response response) {
        super("Push subscription doesn't exist", null, response);
    }
}
