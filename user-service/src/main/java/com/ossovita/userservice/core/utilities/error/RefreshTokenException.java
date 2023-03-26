package com.ossovita.userservice.core.utilities.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RefreshTokenException extends RuntimeException {

    public RefreshTokenException(String msg) {
        super(msg);
    }
}