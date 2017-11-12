package com.solovey.movieland.web.util.auth.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST,
        reason = "Bad login request")
public class BadLoginRequestException extends RuntimeException {
}
