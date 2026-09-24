package com.finova.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OtpAlreadyVerifiedException extends RuntimeException{
    public OtpAlreadyVerifiedException(){
        super("OTP has already been verified");
    }
}
