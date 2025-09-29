package com.nueuda.crudapi.exception;

public class InvalidParamsException extends RuntimeException {

    public InvalidParamsException(String message) {
        super(message);
    }
}