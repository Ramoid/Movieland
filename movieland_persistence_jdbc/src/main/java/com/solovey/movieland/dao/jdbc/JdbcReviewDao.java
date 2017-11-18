package com.solovey.movieland.dao.jdbc;

import com.solovey.movieland.dao.ReviewDao;
import com.solovey.movieland.dao.jdbc.mapper.ReviewRowMapper;
import com.solovey.movieland.entity.Review;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class JdbcReviewDao implements ReviewDao {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private static final ReviewRowMapper REVIEW_ROW_MAPPER = new ReviewRowMapper();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private String getReviewsByMovieIdSql;

    @Autowired
    private String insertReviewSql;

    @Override
    public List<Review> getReviewsByMovieId(int movieId) {
        log.info("Start query to get review by movie  from DB");
        long startTime = System.currentTimeMillis();
        List<Review> reviews = jdbcTemplate.query(getReviewsByMovieIdSql, REVIEW_ROW_MAPPER, movieId);
        log.info("Finish query to get review by movie from DB. It took {} ms", System.currentTimeMillis() - startTime);
        return reviews;
    }

    @Override
    public Review addReview(Review review) {
        log.info("Start inserting review {} into DB", review.toString());
        long startTime = System.currentTimeMillis();
        KeyHolder keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("movieId", review.getMovieId())
                .addValue("userId", review.getUser().getId())
                .addValue("text", review.getText());


        namedParameterJdbcTemplate.update(insertReviewSql, parameters, keyHolder);
        
        review.setId(keyHolder.getKey().intValue());
        log.info("Finish inserting review into DB. It took {} ms", System.currentTimeMillis() - startTime);

        return review;

    }


}


