package com.solovey.movieland.web.security.exceptions;


public class BadLoginRequestException extends RuntimeException {
    public BadLoginRequestException() {
        super("Bad login request");
    }
}
