package com.example.git.demo.controller;

import com.example.git.demo.exception.UserNotFoundException;
import com.example.git.demo.model.ErrorResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = UserNotFoundException.class)
    public ErrorResponseBody handleUserNotFound(UserNotFoundException exception) {
        return new ErrorResponseBody(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }

    @ExceptionHandler(value = HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<ErrorResponseBody> requestMethodNotSupported(HttpMediaTypeNotAcceptableException exception) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ErrorResponseBody response = new ErrorResponseBody(HttpStatus.NOT_ACCEPTABLE.value(), exception.getMessage());
        return new ResponseEntity<>(response, headers, HttpStatus.NOT_ACCEPTABLE);
    }
}
