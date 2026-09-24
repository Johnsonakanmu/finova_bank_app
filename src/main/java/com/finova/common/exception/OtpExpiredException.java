package com.finova.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OtpExpiredException extends RuntimeException{
    public OtpExpiredException(){
        super("Otp has expired");
    }
}
