package com.example.git.demo.controller;

import com.example.git.demo.exception.UserNotFoundException;
import com.example.git.demo.model.ErrorResponseBody;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;

import static org.assertj.core.api.Assertions.assertThat;

public class RestExceptionHandlerTest {

    private RestExceptionHandler exceptionHandler = new RestExceptionHandler();

    @Test
    public void handleUserNotFound_returnErrorResponseBody_whenThrowsUserNotFoundException() {
        UserNotFoundException notFoundException = new UserNotFoundException("test message");
        ErrorResponseBody expectedBody = new ErrorResponseBody(HttpStatus.NOT_FOUND.value(), notFoundException.getMessage());

        ErrorResponseBody actualBody = exceptionHandler.handleUserNotFound(notFoundException);

        assertThat(actualBody).isEqualTo(expectedBody);
    }

    @Test
    public void requestMethodNotSupported_returnErrorResponseBody_whenThrows() {
        HttpMediaTypeNotAcceptableException exception = new HttpMediaTypeNotAcceptableException("test message");
        ErrorResponseBody expectedBody = new ErrorResponseBody(HttpStatus.NOT_ACCEPTABLE.value(), exception.getMessage());

        ResponseEntity<ErrorResponseBody> actualResponse = exceptionHandler.requestMethodNotSupported(exception);

        ErrorResponseBody actualResponseBody = actualResponse.getBody();
        assertThat(actualResponseBody).isEqualTo(expectedBody);
    }
}
