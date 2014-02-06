package com.datasift.client.exceptions;

import io.higgs.http.client.Response;

public class AuthException extends DataSiftException {
    public AuthException(String msg, Response response) {
        super(msg, null);
        this.response = response;
    }
}
