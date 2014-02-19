package com.datasift.client.exceptions;

import com.datasift.client.exceptions.DataSiftException;
import io.higgs.http.client.Response;

public class JsonParsingException extends DataSiftException {
    public JsonParsingException(String msg, Exception cause, Response response) {
        super(msg, cause);
        this.response = response;
    }
}
