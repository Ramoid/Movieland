package com.solovey.movieland.service.impl;


import com.solovey.movieland.dao.UserDao;
import com.solovey.movieland.dao.enums.UserRole;
import com.solovey.movieland.entity.User;
import com.solovey.movieland.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public Optional<User> extractUser(String email) {
        return userDao.extractUser(email);
    }

    @Override
    public List<UserRole> getUserRoles(int userId) {
        return userDao.getUserRoles(userId);
    }
}