package com.wooteco.haveagoodtime.exception;

import org.springframework.http.HttpStatus;

public class HaveagoodtimeException extends RuntimeException {

    private final HttpStatus status;

    public HaveagoodtimeException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
