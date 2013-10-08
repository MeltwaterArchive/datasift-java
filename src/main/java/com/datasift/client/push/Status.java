package com.datasift.client.push;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * DataSift push statuses as documented at http://dev.datasift.com/docs/push/status-codes
 *
 * @author Courtney Robinson <courtney@crlog.info>
 */
public enum Status {
    ACTIVE("active"),
    PAUSED("paused"),
    WAITING_FOR_START("waiting_for_start"),
    RETRYING("retrying"),
    FAILED("failed"),
    FINISHING("finishing"),
    FINISHED("finished"),
    FINISHING_PAUSED("finishing_paused");
    private final String value;

    Status(String value) {
        this.value = value.toLowerCase();
    }

    @JsonValue
    public String val() {
        return value;
    }

    public boolean isPaused() {
        return is(PAUSED);
    }

    public boolean isActive() {
        return is(ACTIVE);
    }

    public boolean isWaitingForStart() {
        return is(WAITING_FOR_START);
    }

    public boolean isRetrying() {
        return is(RETRYING);
    }

    public boolean isFailed() {
        return is(FAILED);
    }

    public boolean isFinishing() {
        return is(FINISHING);
    }

    public boolean isFinishingPaused() {
        return is(FINISHING_PAUSED);
    }

    public boolean isFinished() {
        return is(FINISHED);
    }

    private boolean is(Status s) {
        return s.val().equalsIgnoreCase(val());
    }

    @Override
    public String toString() {
        return val();
    }
}
