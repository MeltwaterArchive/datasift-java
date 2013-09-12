package com.datasift.client.managedsource;

import com.datasift.client.DataSiftResult;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class ManagedSourceLog extends DataSiftResult implements Iterable<ManagedSourceLog.LogMessage> {
    @JsonProperty("log_entries")
    private final List<LogMessage> entries = new ArrayList<LogMessage>();
    @JsonProperty
    private int count;
    @JsonProperty
    private int page;
    @JsonProperty
    private int pages;
    @JsonProperty("per_page")
    private int perPage;

    /**
     * @return all the available sources. May be empty but will never be null
     */
    public List<LogMessage> getEntries() {
        return entries;
    }

    public int getCount() {
        return count;
    }

    public int getPage() {
        return page;
    }

    public int getPages() {
        return pages;
    }

    public int getPerPage() {
        return perPage;
    }

    @Override
    public Iterator<LogMessage> iterator() {
        return entries.iterator();
    }

    public static class LogMessage {
        @JsonProperty
        private String id;
        @JsonProperty("event_time")
        private long eventTime;
        @JsonProperty("success")
        private boolean successful;
        @JsonProperty
        private String message;

        public String getId() {
            return id;
        }

        public long getEventTime() {
            return eventTime;
        }

        public boolean isSuccessful() {
            return successful;
        }

        public String getMessage() {
            return message;
        }
    }
}
