package com.shoppingmall.exception;

public class NotExistCategoryException extends RuntimeException{
    public NotExistCategoryException(String message) {
        super(message);
    }
}
