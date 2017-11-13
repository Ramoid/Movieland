package com.solovey.movieland.service;

import com.solovey.movieland.entity.User;


public interface UserService {
    User extractUser(String password,String email);
}
