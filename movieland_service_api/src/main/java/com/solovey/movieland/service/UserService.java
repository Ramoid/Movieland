package com.solovey.movieland.service;

import com.solovey.movieland.entity.User;

import java.util.Optional;


public interface UserService {
    Optional<User> extractUser(String email);
}
