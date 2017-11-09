package com.solovey.movieland.dao;

import com.solovey.movieland.entity.User;

public interface UserDao {
    void FindAndEnrichUser(User user);
}
