package com.datasift.client.push;

import com.datasift.client.APIDataSiftResult;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class PushLogMessages extends APIDataSiftResult {
    @JsonProperty
    private boolean success;
    @JsonProperty
    private int count;
    @JsonProperty("log_entries")
    private List<PushLogMessage> logMessages;

    public boolean isSuccess() {
        return success;
    }

    public int getCount() {
        return count;
    }

    /**
     * @return all the log messages returned in this result, will never be null but may be empty
     */
    public List<PushLogMessage> getLogMessages() {
        return logMessages == null ? new ArrayList<PushLogMessage>() : logMessages;
    }

    public static class PushLogMessage {
        @JsonProperty("subscription_id")
        private String subscriptionId;
        @JsonProperty
        private boolean success;
        @JsonProperty("request_time")
        private long requestTime;
        @JsonProperty
        private String message;

        public String getSubscriptionId() {
            return subscriptionId;
        }

        public boolean isSuccess() {
            return success;
        }

        public DateTime getRequestTime() {
            return new DateTime(TimeUnit.MILLISECONDS.toSeconds(requestTime));
        }

        public String getMessage() {
            return message;
        }
    }
}
