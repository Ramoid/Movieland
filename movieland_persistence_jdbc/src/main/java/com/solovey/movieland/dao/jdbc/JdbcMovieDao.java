package com.solovey.movieland.dao.jdbc;

import com.solovey.movieland.dao.jdbc.mapper.MovieRowMapper;
import com.solovey.movieland.dao.jdbc.mapper.RandomMovieRowMapper;
import com.solovey.movieland.dao.MovieDao;
import com.solovey.movieland.entity.Movie;
import com.solovey.movieland.entity.enums.SortDirection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class JdbcMovieDao implements MovieDao {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private static final MovieRowMapper MOVIE_ROW_MAPPER = new MovieRowMapper();
    private static final RandomMovieRowMapper RANDOM_MOVIE_ROW_MAPPER = new RandomMovieRowMapper();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private String getMoviesByGenreIdSql;

    @Autowired
    private String getAllMoviesSql;

    @Autowired
    private String getMovieByIdSql;

    @Autowired
    private String getRandomMoviesSql;

    @Autowired
    private JdbcReviewDao jdbcReviewDao;

    @Autowired
    private QueryGenerator queryGenerator;

    @Override
    public List<Movie> getAllMovies(Map<String, SortDirection> sortType) {
        log.info("Start query to get all movies from DB");
        long startTime = System.currentTimeMillis();

        List<Movie> movies = jdbcTemplate.query(queryGenerator.addSorting(getAllMoviesSql, sortType), MOVIE_ROW_MAPPER);
        log.info("Finish query to get all movies from DB. It took {} ms", System.currentTimeMillis() - startTime);
        return movies;
    }

    @Override
    public List<Movie> getRandomMovies() {
        log.info("Start query to get random movies from DB");
        long startTime = System.currentTimeMillis();

        List<Movie> movies = jdbcTemplate.query(getRandomMoviesSql, RANDOM_MOVIE_ROW_MAPPER);

        log.info("Finish query to get random movies from DB. It took {} ms", System.currentTimeMillis() - startTime);
        return movies;
    }

    @Override
    public List<Movie> getMoviesByGenreId(int id, Map<String, SortDirection> sortType) {
        log.info("Start query to get movies by genre from DB");
        long startTime = System.currentTimeMillis();

        List<Movie> movies = jdbcTemplate.query(queryGenerator.addSorting(getMoviesByGenreIdSql, sortType), MOVIE_ROW_MAPPER, id);
        log.info("Finish query to get movies by genre from DB. It took {} ms", System.currentTimeMillis() - startTime);
        return movies;
    }

    @Override
    public Movie getMovieById(int movieId) {
        log.info("Start query to get movie by id from DB");
        long startTime = System.currentTimeMillis();

        Movie movie = jdbcTemplate.queryForObject(getMovieByIdSql, RANDOM_MOVIE_ROW_MAPPER, movieId);

        log.info("Finish query to get movie by id from DB. It took {} ms", System.currentTimeMillis() - startTime);
        return movie;

    }


}


