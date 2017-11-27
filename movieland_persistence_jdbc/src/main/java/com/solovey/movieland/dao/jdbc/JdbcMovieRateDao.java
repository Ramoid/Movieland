package com.solovey.movieland.dao.jdbc;


import com.solovey.movieland.dao.MovieRateDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Set;


@Repository
public class JdbcMovieRateDao implements MovieRateDao {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    @Autowired
    private String inserMovieRateSql;

    @Autowired
    private String updateMoviesRateSql;


    @Override
    public void rateMovie(int userId, int movieId, double rate) {
        log.info("Start inserting movie rate for movie id {} in DB", movieId);
        long startTime = System.currentTimeMillis();
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("movieId", movieId)
                .addValue("userId", userId)
                .addValue("rate", rate);
        namedParameterJdbcTemplate.update(inserMovieRateSql, parameters);
        log.info("Finish inserting movie rate into DB. It took {} ms", System.currentTimeMillis() - startTime);
    }

    @Override
    public void updateMoviesRating(Set<Integer> movieIds) {
        log.info("Start updating  rating for movie ids {} in DB", movieIds);
        long startTime = System.currentTimeMillis();
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("movieIds", movieIds);
        namedParameterJdbcTemplate.update(updateMoviesRateSql, parameters);

        log.info("Finish updating movie ratings in DB. It took {} ms", System.currentTimeMillis() - startTime);

    }
}


