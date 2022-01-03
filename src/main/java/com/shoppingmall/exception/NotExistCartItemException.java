package com.shoppingmall.exception;

public class NotExistCartItemException extends RuntimeException{
    public NotExistCartItemException(String message) {
        super(message);
    }
}
