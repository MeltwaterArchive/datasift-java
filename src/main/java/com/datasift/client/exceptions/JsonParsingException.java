package com.datasift.client.exceptions;

import io.higgs.http.client.Response;

public class JsonParsingException extends DataSiftException {
    public JsonParsingException(String msg, Exception cause, Response response) {
        super(msg, cause);
        this.response = response;
    }
}
