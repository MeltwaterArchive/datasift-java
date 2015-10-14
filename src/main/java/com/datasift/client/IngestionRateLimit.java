package com.datasift.client;

public interface IngestionRateLimit {

    /**
     * @return Maximum number of requests for this account or {@link com.datasift.client.DataSiftClient#DEFAULT_NUM}
     * if the information was not returned
     */
    int requestRateLimit();

    /**
     * @return Requests remaining for this account or
     *         {@link com.datasift.client.DataSiftClient#DEFAULT_NUM} if the information was not returned
     */
    int requestRateLimitRemaining();

    /**
     * @return Time until requests limit is reset to maximum or
     *         {@link com.datasift.client.DataSiftClient#DEFAULT_NUM} if the information was not returned
     */
    int requestRateLimitReset();

    /**
     * @return POSIX time stamp representing the moment at which request limit will reset or
     *         {@link com.datasift.client.DataSiftClient#DEFAULT_NUM} if the information was not returned
     */
    int requestRateLimitResetTTL();

    /**
     * @return Maximum amount of data for this account or
     *         {@link com.datasift.client.DataSiftClient#DEFAULT_NUM} if the information was not returned
     */
    int dataRateLimit();

    /**
     * @return Data remaining for this account or
     *         {@link com.datasift.client.DataSiftClient#DEFAULT_NUM} if the information was not returned
     */
    int dataRateLimitRemaining();

    /**
     * @return Time until data limit is reset to maximum or
     *         {@link com.datasift.client.DataSiftClient#DEFAULT_NUM} if the information was not returned
     */
    int dataRateLimitReset();

    /**
     * @return POSIX time stamp representing the moment at which data limit will reset or
     *         {@link com.datasift.client.DataSiftClient#DEFAULT_NUM} if the information was not returned
     */
    int dataRateLimitResetTTL();
}
