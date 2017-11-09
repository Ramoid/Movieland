package com.solovey.movieland.dao.jdbc.mapper;

import com.solovey.movieland.entity.Genre;
import com.solovey.movieland.entity.Movie;
import com.solovey.movieland.entity.Country;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RandomMovieRowMapper implements RowMapper<Movie> {

    @Override
    public Movie mapRow(ResultSet resultSet, int i) throws SQLException {
        Movie movie = new Movie();
        movie.setMovieId(resultSet.getInt("movie_id"));
        movie.setNameRussian(resultSet.getString("movie_name_russian"));
        movie.setNameNative(resultSet.getString("movie_name_native"));
        movie.setDescription(resultSet.getString("description"));
        movie.setYearOfRelease(resultSet.getInt("movie_year"));
        movie.setRating(resultSet.getDouble("rating"));
        movie.setPrice(resultSet.getDouble("price"));
        movie.setPicturePath(resultSet.getString("link"));

        List<Genre> genres = new ArrayList<>();

        for (String genreId : resultSet.getString("genres_id").split(",")) {
            Genre genre = new Genre();
            genre.setId(Integer.parseInt(genreId));
            genres.add(genre);
        }

        List<Country> countries = new ArrayList<>();

        for (String countryId : resultSet.getString("countries_id").split(",")) {
            Country country = new Country();
            country.setId(Integer.parseInt(countryId));
            countries.add(country);
        }

        movie.setGenres(genres);
        movie.setCountries(countries);

        return movie;
    }
}
