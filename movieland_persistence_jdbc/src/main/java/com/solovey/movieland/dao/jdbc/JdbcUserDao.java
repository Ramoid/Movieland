package com.solovey.movieland.dao.jdbc;


import com.solovey.movieland.dao.UserDao;
import com.solovey.movieland.dao.jdbc.mapper.UserRowMapper;
import com.solovey.movieland.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JdbcUserDao implements UserDao {
    private static final UserRowMapper USER_ROW_MAPPER = new UserRowMapper();
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private String getuserByEmail;

    @Override
    public Optional<User> extractUser(String email) {
        log.info("Start extracting user {} by password and email from DB", email);
        long startTime = System.currentTimeMillis();

        try {
            User user = jdbcTemplate.queryForObject(getuserByEmail, USER_ROW_MAPPER, email);
            log.info("Finish query extract user {} by passoword and email. It took {} ms", user, System.currentTimeMillis() - startTime);
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            log.info("User {} is not found. It took {} ms", email, System.currentTimeMillis() - startTime);
            return Optional.empty();

        }



    }
}


