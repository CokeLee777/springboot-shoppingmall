package com.shoppingmall.exception;

public class DuplicatedCategoryException extends RuntimeException{
    public DuplicatedCategoryException(String message) {
        super(message);
    }
}
