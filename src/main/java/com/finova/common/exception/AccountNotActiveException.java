package com.finova.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AccountNotActiveException extends RuntimeException{

    public AccountNotActiveException(String message) {
        super(message);
    }
}
