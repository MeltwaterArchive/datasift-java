package com.datasift.client.stream;

/*
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public abstract class StreamEventListener {
    public void streamClosed() {
    }

    public void streamOpened() {
    }

    public abstract void onDelete(DeletedInteraction di);

}
