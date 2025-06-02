package com.kata.bankaccountback.advice;

import com.kata.bankaccountback.exceptions.InvalidDataException;
import com.kata.bankaccountback.exceptions.RessourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalControllerAdvice {


    @ExceptionHandler({RessourceNotFoundException.class})
    public ResponseEntity<ApiError> handleNotFoundException(RessourceNotFoundException e) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND.value(), e.getMessage(), LocalDateTime.now());

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({InvalidDataException.class})
    public ResponseEntity<ApiError> handleInvalidDataException(InvalidDataException e) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }


}
