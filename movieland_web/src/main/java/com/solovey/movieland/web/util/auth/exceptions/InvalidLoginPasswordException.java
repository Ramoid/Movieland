package com.solovey.movieland.web.util.auth.exceptions;

public class InvalidLoginPasswordException extends RuntimeException {
    public InvalidLoginPasswordException() {
        super("Invalid loginn/password");
    }
}
