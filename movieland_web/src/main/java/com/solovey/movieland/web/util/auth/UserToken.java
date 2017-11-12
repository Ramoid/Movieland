package com.solovey.movieland.web.util.auth;


public class UserToken {
    private String uuid;
    private String nickname;

    public UserToken(String uuid, String nickname) {
        this.uuid = uuid;
        this.nickname = nickname;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "UserToken{" +
                "uuid='" + uuid + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
