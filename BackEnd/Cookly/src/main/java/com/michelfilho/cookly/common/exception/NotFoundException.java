package com.michelfilho.cookly.common.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(Class<?> c) {
        super("This " + c.getName() +  " doesn't exist!");
    }
}
