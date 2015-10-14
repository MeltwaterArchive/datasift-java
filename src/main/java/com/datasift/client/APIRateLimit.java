package com.datasift.client;

public interface APIRateLimit {

    /**
     * @return How much the rate limit is on this account or
     *         {@link com.datasift.client.DataSiftClient#DEFAULT_NUM} if the information was not returned
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

}
