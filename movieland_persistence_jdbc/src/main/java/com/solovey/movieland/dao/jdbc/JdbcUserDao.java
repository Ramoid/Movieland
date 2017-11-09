package com.solovey.movieland.dao.jdbc;

import com.solovey.movieland.dao.CountryDao;
import com.solovey.movieland.dao.UserDao;
import com.solovey.movieland.dao.jdbc.mapper.CountryRowMapper;
import com.solovey.movieland.entity.Country;
import com.solovey.movieland.entity.Movie;
import com.solovey.movieland.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class JdbcUserDao implements UserDao {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private static final CountryRowMapper COUNTRY_ROW_MAPPER = new CountryRowMapper();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private String getuserByEmail;

    @Override
    public void FindAndEnrichUser(User user) {
        log.info("Start query to get user by passoword and email from DB");
        long startTime = System.currentTimeMillis();
        SqlRowSet sqlRowSet=jdbcTemplate.queryForRowSet(getuserByEmail,user.getPassword(),user.getEmail());
        while (sqlRowSet.next()) {
            user.setId(sqlRowSet.getInt("user_id"));
            user.setNickname(sqlRowSet.getString("user_name"));
        }

        log.info("Finish query to get user by passoword and email. It took {} ms", System.currentTimeMillis() - startTime);
    }
}


