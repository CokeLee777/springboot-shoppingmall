package com.shoppingmall.exception;

public class NotExistItemException extends RuntimeException{
    public NotExistItemException(String message) {
        super(message);
    }
}
