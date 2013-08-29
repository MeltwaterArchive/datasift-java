package com.datasift.client.stream;

import com.datasift.client.core.Stream;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public abstract class StreamSubscription {
    private Stream stream;

    public StreamSubscription(Stream stream) {
        if (stream == null) {
            throw new IllegalArgumentException("Stream can't be null");
        }
        if (stream.hash() == null || stream.hash().isEmpty()) {
            throw new IllegalArgumentException("Invalid stream subscription request, no hash available");
        }
        this.stream = stream;
    }

    public Stream stream() {
        return stream;
    }

    public abstract void onDataSiftWarning(DataSiftMessage di);

    public abstract void onDataSiftError(DataSiftMessage di);

    public abstract void onDataSiftInfoMessage(DataSiftMessage di);

    public abstract void onMessage(Interaction i);

    public void onConnect() {
    }

    public void onClose() {
    }
}
