package com.solovey.movieland.web.security.entity;

import java.security.Principal;

public class PrincipalUser implements Principal {

    private String userName;
    private int userId;

    public PrincipalUser(String userName, int userId) {
        this.userName = userName;
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String getName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
