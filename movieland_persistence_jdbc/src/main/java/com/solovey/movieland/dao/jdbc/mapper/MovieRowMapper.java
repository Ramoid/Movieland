package com.solovey.movieland.dao.jdbc.mapper;

import com.solovey.movieland.entity.Movie;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MovieRowMapper implements RowMapper<Movie> {

    @Override
    public Movie mapRow(ResultSet resultSet, int i) throws SQLException {
        Movie movie = new Movie();
        movie.setMovieId(resultSet.getInt("movie_id"));
        movie.setNameRussian(resultSet.getString("movie_name_russian"));
        movie.setNameNative(resultSet.getString("movie_name_native"));
        movie.setYearOfRelease(resultSet.getInt("movie_year"));
        movie.setRating(resultSet.getDouble("rating"));
        movie.setPrice(resultSet.getDouble("price"));
        movie.setPicturePath(resultSet.getString("link"));
        return movie;
    }
}
