package com.mdeosjr.AlbumTracker.utils;

import com.mdeosjr.AlbumTracker.services.exceptions.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = UserAlreadyExistsException.class)
    public ResponseEntity<ApiError> userAlreadyExistsHandler() {
        ApiError error = new ApiError(409, "User already exists!", new Date());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
}
