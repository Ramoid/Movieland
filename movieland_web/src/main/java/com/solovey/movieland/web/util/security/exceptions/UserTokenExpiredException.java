package com.solovey.movieland.web.util.security.exceptions;

public class UserTokenExpiredException extends RuntimeException {
    public UserTokenExpiredException(){
        super("User token expired.");
    }
}
