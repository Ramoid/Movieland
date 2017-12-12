package com.solovey.movieland.dao.jdbc;


import com.solovey.movieland.dao.MovieRatingDao;
import com.solovey.movieland.entity.Rating;
import com.solovey.movieland.entity.UserMovieRate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class JdbcMovieRatingDao implements MovieRatingDao {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Autowired
    private String inserMovieRateSql;

    @Autowired
    private String getMoviesRatingsSql;


    @Override
    public void flushRatesToPersistence(List<UserMovieRate> rates) {
        log.info("Start inserting movies rates in DB");
        long startTime = System.currentTimeMillis();

        jdbcTemplate.batchUpdate(inserMovieRateSql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                UserMovieRate userMovieRate = rates.get(i);
                ps.setInt(1, userMovieRate.getMovieId());
                ps.setInt(2, userMovieRate.getUserId());
                ps.setDouble(3, userMovieRate.getRating());
            }

            @Override
            public int getBatchSize() {
                return rates.size();
            }
        });


        log.info("Finish inserting movies rates into DB. It took {} ms", System.currentTimeMillis() - startTime);
    }

    @Override
    public Map<Integer, Rating> getMoviesRatings() {
        log.info("Start getting movies ratings from DB");
        long startTime = System.currentTimeMillis();
        Map<Integer, Rating> moviesRatings = new HashMap<>();

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(getMoviesRatingsSql);
        while (sqlRowSet.next()) {
            Rating rating = new Rating(sqlRowSet.getDouble("rating"), sqlRowSet.getInt("cnt"));
            moviesRatings.put(sqlRowSet.getInt("movie_id"), rating);
        }

        log.info("Finish getting movies ratings from DB. It took {} ms", System.currentTimeMillis() - startTime);
        return moviesRatings;
    }


}


