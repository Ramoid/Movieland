package com.solovey.movieland.dao;

import com.solovey.movieland.entity.User;

public interface UserDao {
    User extractUser(String password,String email);
}
