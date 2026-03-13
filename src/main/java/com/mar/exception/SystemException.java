package com.mar.exception;

public class SystemException extends WithCodeException {

    public SystemException(String message) {
        super(message, 500);
    }

}
