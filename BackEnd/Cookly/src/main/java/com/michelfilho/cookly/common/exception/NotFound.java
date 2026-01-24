package com.michelfilho.cookly.common.exception;

public class NotFound extends RuntimeException {
    public NotFound(Class<?> c) {
        super("This " + c.getName() +  " doesn't exist!");
    }
}
