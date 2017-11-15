package com.solovey.movieland.dao.jdbc.mapper;

import com.solovey.movieland.entity.Genre;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GenreRowMapperTest {

    @Test
    public void testGenreRowMapper() throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);

        when(resultSet.getInt("genre_id")).thenReturn(1);
        when(resultSet.getString("genre_name")).thenReturn("Ужосы");

        GenreRowMapper genreRowMapper = new GenreRowMapper();
        Genre actualGenre = genreRowMapper.mapRow(resultSet, 0);
        assertEquals(actualGenre.getId(), 1);
        assertEquals(actualGenre.getName(), "Ужосы");

    }
}