package com.solovey.movieland.dao.jdbc.mapper;

import com.solovey.movieland.entity.Movie;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MovieRowMapperTest {

    @Test
    public void testMovieRowMapper() throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);

        when(resultSet.getInt(any())).thenReturn(1).thenReturn(3000);
        when(resultSet.getDouble(any())).thenReturn(1.1).thenReturn(123.12);
        when(resultSet.getString(any())).thenReturn("Test russian name").thenReturn("Test native name").thenReturn("Test path");

        MovieRowMapper movieMapper = new MovieRowMapper();
        Movie actualMovie = movieMapper.mapRow(resultSet, 0);
        assertEquals(actualMovie.getMovieId(), 1);
        assertEquals(actualMovie.getNameRussian(), "Test russian name");
        assertEquals(actualMovie.getNameNative(), "Test native name");
        assertEquals(actualMovie.getPicturePath(), "Test path");
        assertEquals(actualMovie.getPrice(), 123.12, 0);
        assertEquals(actualMovie.getRating(), 1.1, 0);
    }
}