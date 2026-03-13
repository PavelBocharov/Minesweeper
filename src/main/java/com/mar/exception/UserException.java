package com.mar.exception;

public class UserException extends WithCodeException {

    public UserException(String message) {
        super(message, 400);
    }

}
