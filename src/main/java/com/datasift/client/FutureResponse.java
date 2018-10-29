package com.datasift.client;

/*
 * All requests to DataSift are performed asynchronously.
 * When a response is received to a request at some point in the future, this interface is the main way of reporting
 * the response back to a user.
 *
 * @param <T>
 */
public interface FutureResponse<T> {
    void apply(T data);
}
