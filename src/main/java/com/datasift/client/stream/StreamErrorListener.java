package com.datasift.client.stream;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public abstract class StreamErrorListener {
    public void streamClosed() {
    }

    public void streamOpened() {
    }

    public abstract void exceptionCaught(Throwable t);

    public abstract void onDelete(DeletedInteraction di);

}
