package com.solovey.movieland.dao.jdbc;

import com.solovey.movieland.dao.GenreDao;
import com.solovey.movieland.dao.jdbc.mapper.GenreRowMapper;
import com.solovey.movieland.entity.Genre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcGenreDao implements GenreDao {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private static final GenreRowMapper GENRE_ROW_MAPPER = new GenreRowMapper();


    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Autowired
    private String getAllGenresSql;


    @Override
    public List<Genre> getAllGenres() {
        log.info("Start query to get all genres  from DB");
        long startTime = System.currentTimeMillis();
        List<Genre> genres = jdbcTemplate.query(getAllGenresSql, GENRE_ROW_MAPPER);
        log.info("Finish query to get all genres from DB. It took {} ms", System.currentTimeMillis() - startTime);
        return genres;
    }


}


