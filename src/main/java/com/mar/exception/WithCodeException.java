package com.mar.exception;

import lombok.Getter;

public class WithCodeException extends RuntimeException {

    @Getter
    private final Integer code;

    public WithCodeException(String message, Integer code) {
        super(message);
        this.code = code;
    }
}
