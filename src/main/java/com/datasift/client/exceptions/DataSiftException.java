package com.datasift.client.exceptions;

import io.higgs.http.client.Response;

public class DataSiftException extends RuntimeException {
    protected Response response;

    public DataSiftException(String msg) {
        super(msg);
    }

    public DataSiftException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public DataSiftException(String s, Throwable throwable, Response response) {
        this(s, throwable);
        this.response = response;
    }

    /**
     * Gets the response which cause the exception, IF AVAILABLE.
     * Check if this returns null
     *
     * @return the response which resulted in the exception
     */
    public Response getResponse() {
        return response;
    }
}
