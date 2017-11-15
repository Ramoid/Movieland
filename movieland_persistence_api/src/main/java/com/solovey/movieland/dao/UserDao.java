package com.solovey.movieland.dao;

import com.solovey.movieland.entity.User;

import java.util.Optional;

public interface UserDao {
    Optional<User> extractUser(String email);
}
