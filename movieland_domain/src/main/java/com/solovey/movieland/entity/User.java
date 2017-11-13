package com.solovey.movieland.entity;


import java.time.LocalDateTime;

public class User {
    private int id;
    private String nickname;
    private String email;
    private String password;
    private LocalDateTime tokenGeneratedDateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getTokenGeneratedDateTime() {
        return tokenGeneratedDateTime;
    }

    public void setTokenGeneratedDateTime(LocalDateTime tokenGeneratedDateTime) {
        this.tokenGeneratedDateTime = tokenGeneratedDateTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +

                '}';
    }
}
