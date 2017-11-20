package com.solovey.movieland.dao.jdbc.mapper;

import com.solovey.movieland.entity.Movie;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RandomMovieRowMapperTest {

    @Test
    public void testRandomMovieRowMapper() throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);

        when(resultSet.getInt("movie_id")).thenReturn(1);
        when(resultSet.getInt("movie_year")).thenReturn(3000);
        when(resultSet.getDouble("rating")).thenReturn(1.1);
        when(resultSet.getDouble("price")).thenReturn(123.12);
        when(resultSet.getString("movie_name_russian")).thenReturn("Test russian name");
        when(resultSet.getString("movie_name_native")).thenReturn("Test native name");
        when(resultSet.getString("link")).thenReturn("Test path");
        when(resultSet.getString("description")).thenReturn("Test description");
        when(resultSet.getString("genres_id")).thenReturn("1,2");
        when(resultSet.getString("countries_id")).thenReturn("1,2");

        RandomMovieRowMapper randomMovieRowMapper = new RandomMovieRowMapper();
        Movie actualMovie = randomMovieRowMapper.mapRow(resultSet, 0);
        assertEquals(actualMovie.getMovieId(), 1);
        assertEquals(actualMovie.getNameRussian(), "Test russian name");
        assertEquals(actualMovie.getNameNative(), "Test native name");
        assertEquals(actualMovie.getPicturePath(), "Test path");
        assertEquals(actualMovie.getPrice(), 123.12, 0);
        assertEquals(actualMovie.getRating(), 1.1, 0);
        assertEquals(actualMovie.getYearOfRelease(), new Integer(3000));
        assertEquals(actualMovie.getDescription(), "Test description");
        int countryId = actualMovie.getCountries().get(0).getId();
        assertEquals(countryId, 1);
        int genreId = actualMovie.getGenres().get(0).getId();
        assertEquals(genreId, 1);
    }
}