package com.datasift.client.examples;

import com.datasift.client.DataSiftClient;
import com.datasift.client.DataSiftConfig;
import com.datasift.client.managedsource.ManagedSource;
import com.datasift.client.managedsource.sources.TwitterGnip;
import com.datasift.client.odp.ODPBatchResponse;

public class ODPApi {
    private ODPApi() throws InterruptedException {
        DataSiftConfig config = new DataSiftConfig("username", "api-key");

        final DataSiftClient datasift = new DataSiftClient(config);

        /**
         * ODP mapping "gnip_1" provides an ingestion mapping for processing tweets from Gnip.
         * Instantiating TwitterGnip will automatically use the IDML required for sending to a TwitterGnip source.
         */
        System.out.println("Creating twitter_gnip source");
        TwitterGnip source = new TwitterGnip(config);
        ManagedSource managedSource = datasift.managedSource().create("twitter_gnip", source).sync();
        System.out.println("twitter_gnip source created with ID: " + managedSource.getId());

        String data = "{\"id\":\"first_interaction\"}\n{\"id\":\"second_interaction\"}";
        ODPBatchResponse batchResult = datasift.odp().batch(managedSource.getId(), data).sync();
        System.out.println("Batch response: " + batchResult.toString());

        if (!batchResult.isSuccessful()) {
            // If true an exception may have caused the request to fail, inspect the cause if available
            if (batchResult.failureCause() != null) { // May not be an exception
                batchResult.failureCause().printStackTrace();
            }
            return;
        }

        // Check for authorization failure
        batchResult.isAuthorizationSuccessful();
        // Access the low level response object
        batchResult.getResponse();

        // Request rate max limit
        batchResult.rateLimit();
        // Requests remaining
        batchResult.rateLimitRemaining();
        // Time before request limit will be reset
        batchResult.rateLimitReset();
        // POSIX timestamp representing time at which request limit resets
        batchResult.rateLimitResetTTL();
        // Data rate max limit
        batchResult.dataRateLimit();
        // Data remaining
        batchResult.dataRateLimitRemaining();
        // Time before data limit will be reset
        batchResult.dataRateLimitReset();
        // POSIX timestamp representing time at which data limit resets
        batchResult.dataRateLimitResetTTL();

        // Get number of accepted interactions
        batchResult.getInteractionsProcessed();
        // Get number of bytes accepted and processed
        batchResult.getBytesProcessed();

        System.out.println("Removing example source: " + managedSource.getId());
        datasift.managedSource().delete(managedSource.getId());
    }

    public static void main(String... args) throws InterruptedException {
        new ODPApi();
    }
}
