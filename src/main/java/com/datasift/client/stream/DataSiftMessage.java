package com.datasift.client.stream;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class DataSiftMessage {
    protected long tick;
    protected String status;
    protected String message;

    public DataSiftMessage(MultiStreamInteraction mi) {
        tick = mi.getTick();
        status = mi.getStatus();
        message = mi.getMessage();
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
}
