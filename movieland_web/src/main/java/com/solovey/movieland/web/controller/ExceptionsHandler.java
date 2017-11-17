package com.solovey.movieland.web.controller;

import com.solovey.movieland.web.util.dto.ExceptionDto;
import com.solovey.movieland.web.util.security.exceptions.BadLoginRequestException;
import com.solovey.movieland.web.util.security.exceptions.InvalidLoginPasswordException;
import com.solovey.movieland.web.util.security.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionsHandler {
    @ExceptionHandler(BadLoginRequestException.class)
    public ResponseEntity<?> badLoginHandler(BadLoginRequestException e){
        return new ResponseEntity<>(new ExceptionDto(e.getMessage()),  HttpStatus.BAD_REQUEST);

    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> userNotFoundHandler(UserNotFoundException e){
        return new ResponseEntity<>(new ExceptionDto(e.getMessage()),  HttpStatus.UNAUTHORIZED);

    }
    @ExceptionHandler(InvalidLoginPasswordException.class)
    public ResponseEntity<?> innvalidLoginHandler(InvalidLoginPasswordException e){
        return new ResponseEntity<>(new ExceptionDto(e.getMessage()),  HttpStatus.UNAUTHORIZED);

    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> runtimeExceptionHandler(RuntimeException e){
        return new ResponseEntity<>(new ExceptionDto(e.getMessage()),  HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
