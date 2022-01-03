package com.shoppingmall.exception;

public class NotExistCartException extends RuntimeException{
    public NotExistCartException(String message) {
        super(message);
    }
}
