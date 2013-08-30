package com.datasift.client.stream;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public abstract class ErrorListener {
    public abstract void exceptionCaught(Throwable t);
}
