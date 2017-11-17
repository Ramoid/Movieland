package com.solovey.movieland.service;

import com.solovey.movieland.dao.enums.UserRole;
import com.solovey.movieland.entity.User;

import java.util.List;
import java.util.Optional;


public interface UserService {
    Optional<User> extractUser(String email);
    List<UserRole> getUserRoles(int userId);
}
