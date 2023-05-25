package com.ossovita.commonservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class UnexpectedRequestException extends RuntimeException {

    public UnexpectedRequestException() {
        super();
    }

    public UnexpectedRequestException(String message) {
        super(message);
    }
}
