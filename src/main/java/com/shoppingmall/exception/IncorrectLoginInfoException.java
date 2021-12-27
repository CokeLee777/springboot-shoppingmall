package com.shoppingmall.exception;

public class IncorrectLoginInfoException extends RuntimeException{
    public IncorrectLoginInfoException(String message) {
        super(message);
    }
}
