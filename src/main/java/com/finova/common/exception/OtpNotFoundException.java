package com.finova.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OtpNotFoundException extends RuntimeException{

    public OtpNotFoundException() {
        super("OTP not found");
    }
}
