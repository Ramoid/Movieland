package com.solovey.movieland.dao;

import com.solovey.movieland.dao.enums.UserRole;
import com.solovey.movieland.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> extractUser(String email);
    List<UserRole> getUserRoles(int userId);
}
