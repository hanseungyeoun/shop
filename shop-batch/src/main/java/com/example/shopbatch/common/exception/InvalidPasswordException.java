package com.example.shopbatch.common.exception;

public class InvalidPasswordException extends BaseException {
    public InvalidPasswordException() {
        super(ErrorCode.INVALID_PASSWORD);
    }

    public InvalidPasswordException(String message) {
        super(message, ErrorCode.INVALID_PASSWORD);
    }
}
