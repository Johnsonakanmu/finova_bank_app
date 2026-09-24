package com.finova.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class PhoneNumberAlreadyExistsException extends RuntimeException{

    public PhoneNumberAlreadyExistsException(String phoneNumber) {
        super("User already exists with the Phone Number: " + phoneNumber);
    }
}
