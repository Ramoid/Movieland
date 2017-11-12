package com.solovey.movieland.dao.jdbc;

import com.solovey.movieland.dao.GenreDao;
import com.solovey.movieland.dao.ReviewDao;
import com.solovey.movieland.dao.jdbc.mapper.GenreRowMapper;
import com.solovey.movieland.dao.jdbc.mapper.ReviewRowMapper;
import com.solovey.movieland.entity.Genre;
import com.solovey.movieland.entity.Movie;
import com.solovey.movieland.entity.Review;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class JdbcReviewDao implements ReviewDao {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private static final ReviewRowMapper REVIEW_ROW_MAPPER = new ReviewRowMapper();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private String getReviewsByMovieIdSql;

    @Override
    public List<Review> getReviewsByMovieId(int movieId) {
        log.info("Start query to get review by movie  from DB");
        long startTime = System.currentTimeMillis();
        List<Review> reviews = jdbcTemplate.query(getReviewsByMovieIdSql, REVIEW_ROW_MAPPER, movieId);
        log.info("Finish query to get review by movie from DB. It took {} ms", System.currentTimeMillis() - startTime);
        return reviews;
    }


}


