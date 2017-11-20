package com.solovey.movieland.dao.jdbc;

import com.solovey.movieland.dao.GenreDao;
import com.solovey.movieland.dao.jdbc.mapper.GenreRowMapper;
import com.solovey.movieland.entity.Genre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class JdbcGenreDao implements GenreDao {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private static final GenreRowMapper GENRE_ROW_MAPPER = new GenreRowMapper();


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    @Autowired
    private String getAllGenresSql;

    @Autowired
    private String insertGenreMappingSql;

    @Autowired
    private String deleteGenreMappingsByIdsSql;


    @Override
    public List<Genre> getAll() {
        log.info("Start query to get all genres  from DB");
        long startTime = System.currentTimeMillis();
        List<Genre> genres = jdbcTemplate.query(getAllGenresSql, GENRE_ROW_MAPPER);
        log.info("Finish query to get all genres from DB. It took {} ms", System.currentTimeMillis() - startTime);
        return genres;
    }

    @Override
    public void addGenreMapping(List<Genre> genres, int movieId) {
        log.info("Start inserting genres {} into genre_mappring", genres);
        long startTime = System.currentTimeMillis();
        jdbcTemplate.batchUpdate(insertGenreMappingSql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Genre genre = genres.get(i);
                ps.setInt(1, movieId);
                ps.setInt(2, genre.getId());
            }

            @Override
            public int getBatchSize() {
                return genres.size();
            }
        });

        log.info("Finish inserting genre mappings into DB. It took {} ms", System.currentTimeMillis() - startTime);

    }

    @Override
    public void removeGenreMappingsByIds(List<Genre> genres, int movieId) {
        log.info("Start deleting genres {} form genre_mappring", genres);
        long startTime = System.currentTimeMillis();
        List<Integer> genreIds = new ArrayList<>();
        for (Genre genre : genres) {
            genreIds.add(genre.getId());
        }
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("movieId", movieId)
                .addValue("genreIds", genreIds);
        namedParameterJdbcTemplate.update(deleteGenreMappingsByIdsSql, parameters);
        log.info("Finish deleting genre mappings from DB. It took {} ms", System.currentTimeMillis() - startTime);


    }


}


