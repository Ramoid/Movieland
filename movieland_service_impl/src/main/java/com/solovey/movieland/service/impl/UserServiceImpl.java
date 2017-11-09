package com.solovey.movieland.service.impl;


import com.solovey.movieland.dao.UserDao;
import com.solovey.movieland.entity.User;
import com.solovey.movieland.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;


    @Override
    public void FindAndEnrichUser(User user) {
        userDao.FindAndEnrichUser(user);
    }
}