package com.solovey.movieland.web.util.auth.exceptions;


public class BadLoginRequestException extends RuntimeException {
    public BadLoginRequestException() {
        super("Bad login request");
    }
}
