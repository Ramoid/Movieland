package com.solovey.movieland.web.security.exceptions;

public class UserTokenExpiredException extends RuntimeException {
    public UserTokenExpiredException(){
        super("User token expired.");
    }
}
