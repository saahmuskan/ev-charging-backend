package com.evcharging.exception;

public class StationException extends Exception {
    public StationException(String message) {
        super(message);
    }

    public StationException(String message, Throwable cause) {
        super(message, cause);
    }
}