package com.solovey.movieland.web.util.security.exceptions;

public class InvalidLoginPasswordException extends RuntimeException {
    public InvalidLoginPasswordException() {
        super("Invalid loginn/password");
    }
}
