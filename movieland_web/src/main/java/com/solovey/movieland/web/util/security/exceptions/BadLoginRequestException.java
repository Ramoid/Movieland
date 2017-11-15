package com.solovey.movieland.web.util.security.exceptions;


public class BadLoginRequestException extends RuntimeException {
    public BadLoginRequestException() {
        super("Bad login request");
    }
}
