package com.epam.esm.exception;


import org.springframework.validation.Errors;

public class RestControllerException extends RuntimeException {
    private String errorCode;
    private Errors errors;

    public RestControllerException() {
        super();
    }

    public RestControllerException(String message) {
        super(message);
    }

    public RestControllerException(String message, Throwable cause) {
        super(message, cause);
    }

    public RestControllerException(Throwable cause) {
        super(cause);
    }

    public RestControllerException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public RestControllerException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    public RestControllerException(String message, String errorCode, Errors errors) {
        super(message);
        this.errorCode = errorCode;
        this.errors = errors;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public Errors getErrors() {
        return errors;
    }
}
