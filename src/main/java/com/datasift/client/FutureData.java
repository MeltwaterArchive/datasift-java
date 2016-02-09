package com.datasift.client;

import com.datasift.client.exceptions.DataSiftException;
import com.datasift.client.util.WrappedResponse;
import org.cliffc.high_scale_lib.NonBlockingHashSet;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class FutureData<T> {
    protected NonBlockingHashSet<FutureResponse<T>> listeners = new NonBlockingHashSet<FutureResponse<T>>();
    protected T data;
    protected Throwable interruptCause;
    protected final BlockingQueue<Object> block = new LinkedBlockingDeque<>();

    /**
     * Wraps any object in a {@link FutureData} instance
     * <p/>
     * Intended use is to enable any object obtained without a future to be passed to API methods.
     * This allows API methods to accept FutureData objects and alleviates the need for a user to add
     * many callbacks and instead just pass futures around as if they were already obtained values.
     * Note that any listeners added will be immediately invoked/notified, seeing as the result they would
     * await has already been obtained
     *
     * @param obj the object to wrap
     * @param <A> the type of the object
     * @return a future that will fire onData events for the given object
     */
    public static <A extends BaseDataSiftResult> FutureData<A> wrap(A obj) {
        if (obj == null) {
            throw new IllegalArgumentException("You cannot wrap null as future data");
        }
        FutureData<A> future = new FutureData<A>();
        obj.setResponse(new WrappedResponse());
        future.data = obj;
        return future;
    }

    /**
     * Invoked when a response is received and this future data is now available for use/processing
     *
     * @param data the data received or an object wrapping said data
     */
    public void received(T data) {
        //lock on block and make modification of data and the notification "atomic"
        synchronized (block) {
            this.data = data;
            notifyListeners();
            doNotify();
        }
    }

    /**
     * Adds an event listener that is notified when data is received
     *
     * @param response the future which should listen for a response
     * @return this to enable chaining
     */
    public FutureData<T> onData(FutureResponse<T> response) {
        this.listeners.add(response);
        //if we already received a response then notify straight away
        if (this.data != null) {
            response.apply(this.data);
            doNotify();
        }
        return this;
    }

    protected void notifyListeners() {
        for (FutureResponse<T> res : listeners) {
            res.apply(data);
        }
    }

    public void doNotify() {
        //synchronized (this) {
        //notify();
        //}
        //lock's re-entrant so not a problem between received -> doNotify but external calls to doNotify must also obtain lock on block
        synchronized (block) {
            block.add(new Object());
        }
    }

    /**
     * Forces the client to wait until a response is received before returning
     *
     * @return a result instance - if an interrupt exception is thrown it is possible that a response isn't available
     * yet the user must check to ensure null isn't returned
     */
    public T sync() {
        //if data is present there's no need to block
        synchronized (block) {
            if (data != null) {
                return data;
            }
            block.clear();
        }
        synchronized (this) {
            try {
                // wait();
                block.take();
            } catch (InterruptedException e) {
                if (interruptCause == null) {
                    interruptCause = e;
                }
            }
            if (interruptCause != null) {
                if (interruptCause instanceof DataSiftException) {
                    throw (DataSiftException) interruptCause;
                } else {
                    throw new DataSiftException("Interrupted while waiting for response", interruptCause);
                }
            }
            return data;
        }
    }

    public void interuptCause(Throwable cause) {
        this.interruptCause = cause;
    }
}
