package com.michelfilho.cookly.common.exception;

public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException() {
        super("Your token is invalid");
    }

}
