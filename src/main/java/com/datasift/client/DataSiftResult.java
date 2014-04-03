package com.datasift.client;

/**
 * @author Courtney Robinson <courtney@crlog.info>
 */
public interface DataSiftResult {
    /**
     * A response is considered successful if if a response has been received, the response doesn't contain an error
     * message AND the HTTP response status code is 200 to 399 (i.e. OKs or redirects)
     *
     * @return true if this API result was successful.
     */
    boolean isSuccessful();

    Response getResponse();

    void setResponse(Response response);

    /**
     * @return If the request has failed, this returns the reason for the failure
     *         May* be null
     */
    Throwable failureCause();

    /**
     * @return True if the response status is anything but a 401 Unauthorized
     */
    boolean isAuthorizationSuccesful();

    /**
     * @return How much the rate limit is on this account {@link com.datasift.client.DataSiftClient#DEFAULT_NUM} if the information
     *         was not returned
     */
    int rateLimit();

    /**
     * @return How much is left of the rate limit quota or
     *         {@link com.datasift.client.DataSiftClient#DEFAULT_NUM} if the information was not returned
     */
    int rateLimitRemaining();

    /**
     * Not all API calls are created equally.
     *
     * @return This tells you how much of your rate limit it took to generate this result or
     *         {@link com.datasift.client.DataSiftClient#DEFAULT_NUM} if the information was not returned
     */
    int rateLimitCost();

    /**
     * @return if {@link #isSuccessful()} == false then this returns the error message DataSift returned or null if true
     */
    String getError();

    void failed(Throwable e);

    void successful();
}
