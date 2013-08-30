package com.datasift.client.stream;

import com.datasift.client.core.Stream;
import org.cliffc.high_scale_lib.NonBlockingHashSet;

import java.util.Set;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class DataSiftMessage {
    protected long tick;
    protected String status;
    protected String message;
    protected Set<Stream> hashes = new NonBlockingHashSet<Stream>();

    public DataSiftMessage(MultiStreamInteraction mi) {
        tick = mi.getTick();
        status = mi.getStatus();
        message = mi.getMessage();
        if (mi.hasHashes()) {
            for (String hash : mi.hashes()) {
                hashes.add(Stream.fromString(hash));
            }
        }
    }

    public boolean isInfo() {
        return !isError() && !isWarning();
    }

    public boolean isWarning() {
        return "warning".equals(status);
    }

    public boolean isError() {
        return "failure".equals(status);
    }

    public long getTick() {
        return tick;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "DataSiftMessage{" +
                "tick=" + tick +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    /**
     * @return true if this message has at least 1 stream hash associated with it
     */
    public boolean hashHashes() {
        return hashes.size() > 0;
    }

    /**
     * @return A thread safe set of streams this message is related to
     */
    public Set<Stream> hashes() {
        return hashes;
    }
}
