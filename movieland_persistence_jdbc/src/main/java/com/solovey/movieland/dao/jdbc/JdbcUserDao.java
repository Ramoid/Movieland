package com.solovey.movieland.dao.jdbc;


import com.solovey.movieland.dao.UserDao;
import com.solovey.movieland.dao.jdbc.mapper.CountryRowMapper;
import com.solovey.movieland.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcUserDao implements UserDao {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private String getuserByEmail;

    @Override
    public User extractUser(String password, String email) {
        log.info("Start extracting user {} by password and email from DB", email);
        long startTime = System.currentTimeMillis();

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(getuserByEmail, password, email);

        while (sqlRowSet.next()) {
            User user = new User();
            user.setId(sqlRowSet.getInt("user_id"));
            user.setNickname(sqlRowSet.getString("user_name"));
            user.setPassword(password);
            user.setEmail(email);
            log.info("Finish query extract user {} by passoword and email. It took {} ms", user, System.currentTimeMillis() - startTime);
            return user;
        }

        return null;
    }
}


