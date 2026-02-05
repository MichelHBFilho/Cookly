package com.michelfilho.cookly.common.controller;

import com.michelfilho.cookly.common.dto.ReadErrorDTO;
import com.michelfilho.cookly.common.exception.InvalidTokenException;
import com.michelfilho.cookly.common.exception.InvalidPostInteractionStateException;
import com.michelfilho.cookly.common.exception.NotFoundException;
import com.michelfilho.cookly.common.exception.UsernameAlreadyRegisteredException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UsernameAlreadyRegisteredException.class)
    private ResponseEntity usernameAlreadyRegistered(
            UsernameAlreadyRegisteredException exception
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ReadErrorDTO(
                        400,
                        exception.getMessage()
                )
        );
    }

    @ExceptionHandler(NotFoundException.class)
    private ResponseEntity postNotFound(
            NotFoundException exception
    ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ReadErrorDTO(
                        404,
                        exception.getMessage()
                )
        );
    }

    @ExceptionHandler(InvalidTokenException.class)
    private ResponseEntity invalidToken(
            InvalidTokenException exception
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ReadErrorDTO(
                        400,
                        exception.getMessage()
                )
        );
    }

    @ExceptionHandler(InvalidPostInteractionStateException.class)
    private ResponseEntity postAlreadyLiked(
            InvalidPostInteractionStateException exception
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ReadErrorDTO(
                        400,
                        exception.getMessage()
                )
        );
    }

}
