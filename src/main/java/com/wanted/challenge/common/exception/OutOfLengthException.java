package com.wanted.challenge.common.exception;

public class OutOfLengthException extends RuntimeException {

    public OutOfLengthException() {
    }

    public OutOfLengthException(String message) {
        super(message);
    }

    public OutOfLengthException(String message, Throwable cause) {
        super(message, cause);
    }

    public OutOfLengthException(Throwable cause) {
        super(cause);
    }

    public OutOfLengthException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
