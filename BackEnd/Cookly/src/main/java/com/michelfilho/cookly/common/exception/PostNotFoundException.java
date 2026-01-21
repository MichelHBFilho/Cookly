package com.michelfilho.cookly.common.exception;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException() {
        super("This post doesn't exist!");
    }
}
