package com.solovey.movieland.web.util.auth.exceptions;

public class UserTokenExpiredException extends RuntimeException {
    public UserTokenExpiredException(){
        super("User token expired.");
    }
}
