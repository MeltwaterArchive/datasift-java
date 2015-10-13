package com.datasift.client;

public interface DataSiftIngestionRateLimit {

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
    int requestRateReset();

    /**
     * @return POSIX time stamp representing the moment at which request limit will reset or
     *         {@link com.datasift.client.DataSiftClient#DEFAULT_NUM} if the information was not returned
     */
    int requestRateResetTTL();

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
    int dataRateReset();

    /**
     * @return POSIX time stamp representing the moment at which data limit will reset or
     *         {@link com.datasift.client.DataSiftClient#DEFAULT_NUM} if the information was not returned
     */
    int dataRateResetTTL();
}
