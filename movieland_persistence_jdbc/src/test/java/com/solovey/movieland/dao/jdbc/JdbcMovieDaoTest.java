package com.solovey.movieland.dao.jdbc;

import com.solovey.movieland.dao.jdbc.config.DataConfig;
import com.solovey.movieland.dao.MovieDao;
import com.solovey.movieland.entity.Movie;
import com.solovey.movieland.entity.enums.SortDirection;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DataConfig.class})
public class JdbcMovieDaoTest {
    @Autowired
    MovieDao movieDao;

    @Test
    public void testGelAllMovies() {
        Map<String, SortDirection> sortType = new HashMap<>();
        sortType.put("rating", SortDirection.ASC);
        sortType.put("price", SortDirection.NOSORT);
        List<Movie> movies = movieDao.getAllMovies(sortType);
        assertTrue(movies.size() > 0);
        assertNotNull(movies.get(0).getMovieId());
        assertNotNull(movies.get(0).getNameRussian());
        assertNotNull(movies.get(0).getNameNative());
        assertNotNull(movies.get(0).getYearOfRelease());
        assertNotNull(movies.get(0).getPrice());
        assertNotNull(movies.get(0).getRating());
        assertNull(movies.get(0).getDescription());
    }

    @Test
    public void testGetRandomMovies() {
        List<Movie> movies = movieDao.getRandomMovies();
        assertTrue(movies.size() > 0);
        assertNotNull(movies.get(0).getMovieId());
        assertNotNull(movies.get(0).getNameRussian());
        assertNotNull(movies.get(0).getNameNative());
        assertNotNull(movies.get(0).getYearOfRelease());
        assertNotNull(movies.get(0).getPrice());
        assertNotNull(movies.get(0).getRating());
        assertNotNull(movies.get(0).getDescription());
        assertTrue(movies.get(0).getGenres().size()>0);
        assertTrue(movies.get(0).getCountries().size()>0);
    }
}
