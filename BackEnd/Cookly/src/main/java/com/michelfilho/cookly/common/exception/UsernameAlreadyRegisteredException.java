package com.michelfilho.cookly.common.exception;

public class UsernameAlreadyRegisteredException extends RuntimeException {

    public UsernameAlreadyRegisteredException() {
        super("This username was already taken.");
    }

    public UsernameAlreadyRegisteredException(String username) {
        super("The username " + username + " was already taken.");
    }

}
