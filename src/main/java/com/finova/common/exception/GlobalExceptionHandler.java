package com.finova.common.exception;

import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException exception,
                                                                        WebRequest webRequest){

        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                exception.getMessage(),
                webRequest.getDescription(false),
                "USER_NOT-FOUND"

        );
        return  new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(Exception exception,
                                                              WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                exception.getMessage(),
                webRequest.getDescription(false),
                "INTERNAL SERVER ERROR"
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        List<ObjectError> errorList = ex.getBindingResult().getAllErrors();

        errorList.forEach((error) ->{
            String fileName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fileName, message);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ErrorDetails> handleInsufficientBalance(
            InsufficientBalanceException ex, WebRequest request) {

        ErrorDetails error = new ErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false),
                "INSUFFICIENT_BALANCE"
        );
        return new  ResponseEntity<>(error, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class,
            DisabledException.class, LockedException.class})
    public ResponseEntity<ErrorDetails> handleAuthenticationException(Exception ex, WebRequest request) {

        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                ex.getMessage() != null ? ex.getMessage() : "Invalid username or password",
                request.getDescription(false),
                "LOGIN_FAILED"
        );

        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDetails> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                ex.getMessage() != null ? ex.getMessage() : "Invalid login request",
                request.getDescription(false),
                "LOGIN_FAILED"
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorDetails> handleEmailAlreadyExists(
            EmailAlreadyExistsException ex, WebRequest request) {

        ErrorDetails error = new ErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false),
                "CONFLICT"
        );
        return new  ResponseEntity<>(error, HttpStatus.CONFLICT);

    }

    @ExceptionHandler(PhoneNumberAlreadyExistsException.class)
    public ResponseEntity<ErrorDetails> handlePhoneNumberAlreadyExists(
            PhoneNumberAlreadyExistsException ex, WebRequest request) {

        ErrorDetails error = new ErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false),
                "CONFLICT"
        );
        return new  ResponseEntity<>(error, HttpStatus.CONFLICT);

    }

    @ExceptionHandler(AccountStatusException.class)
    public ResponseEntity<ErrorDetails> handleAccountStatusExists(
            AccountStatusException ex, WebRequest request) {

        ErrorDetails error = new ErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false),
                "FORBIDDEN"
        );
        return new  ResponseEntity<>(error, HttpStatus.FORBIDDEN);

    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorDetails> handleInvalidCredentials(
           InvalidCredentialsException ex, WebRequest request) {

        ErrorDetails error = new ErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false),
                "UNAUTHORIZED"
        );
        return new  ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);

    }

    @ExceptionHandler(OtpNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleOtpException(
            OtpNotFoundException ex, WebRequest request) {

        ErrorDetails error = new ErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false),
                "NOT-FOUND"
        );
        return new  ResponseEntity<>(error, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(InvalidOtpException.class)
    public ResponseEntity<ErrorDetails> handleInvalidOtpException(
            InvalidOtpException ex, WebRequest request) {

        ErrorDetails error = new ErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false),
                "BAD-REQUEST"
        );
        return new  ResponseEntity<>(error, HttpStatus.BAD_REQUEST);

    }


    @ExceptionHandler(OtpAlreadyVerifiedException.class)
    public ResponseEntity<ErrorDetails> handleOtpAlreadyVerifyException(
            OtpAlreadyVerifiedException ex, WebRequest request) {

        ErrorDetails error = new ErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false),
                "BAD-REQUEST"
        );
        return new  ResponseEntity<>(error, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(OtpExpiredException.class)
    public ResponseEntity<ErrorDetails> handleOtpExpireException(
            OtpExpiredException ex, WebRequest request) {

        ErrorDetails error = new ErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false),
                "BAD-REQUEST"
        );
        return new  ResponseEntity<>(error, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ErrorDetails> handleInvalidPasswordException(
            InvalidPasswordException ex, WebRequest request) {

        ErrorDetails error = new ErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false),
                "BAD-REQUEST"
        );
        return new  ResponseEntity<>(error, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(AccountAlreadyExistsException.class)
    public ResponseEntity<ErrorDetails> handleAccountAlreadyExists(
            AccountAlreadyExistsException ex, WebRequest request) {

        ErrorDetails error = new ErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false),
                "CONFLICT"
        );
        return new  ResponseEntity<>(error, HttpStatus.CONFLICT);

    }

    @ExceptionHandler(AccountNotActiveException.class)
    public ResponseEntity<ErrorDetails> handleAccountNotActiveExists(
           AccountNotActiveException ex, WebRequest request) {

        ErrorDetails error = new ErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false),
                "CONFLICT"
        );
        return new  ResponseEntity<>(error, HttpStatus.CONFLICT);

    }


}

