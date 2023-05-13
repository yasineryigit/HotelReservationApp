package com.ossovita.commonservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class RoomNotAvailableException extends RuntimeException{

    public RoomNotAvailableException(String message) {
        super(message);
    }
}
