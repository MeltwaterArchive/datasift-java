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

    public abstract void onDataSiftLogMessage(DataSiftMessage dm);

    public abstract void onMessage(Interaction i);

    public void onConnect() {
    }

    public void onClose() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StreamSubscription that = (StreamSubscription) o;

        if (stream != null ? !stream.equals(that.stream) : that.stream != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return stream != null ? stream.hashCode() : 0;
    }
}
