package com.solovey.movieland.service.impl;


import com.solovey.movieland.dao.GenreDao;
import com.solovey.movieland.entity.Genre;

import com.solovey.movieland.entity.Movie;
import org.junit.Before;
import org.junit.Test;


import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class GenreServiceImplTest {

    private List<Genre> actualGenres;

    @Before
    public void before() {
        actualGenres = createGenreList();
    }

    @Test
    public void enrichMovieWithGenresTest() throws Exception {

        Movie actualMovie = createMovieList().get(0);

        GenreDao mockGenreDao = mock(GenreDao.class);
        when(mockGenreDao.getAllGenres()).thenReturn(actualGenres);

        GenreServiceImpl genreService = new GenreServiceImpl(mockGenreDao);

        assertNull(actualMovie.getGenres().get(0).getName());

        genreService.enrichMovieWithGenres(actualMovie);

        assertEquals(actualMovie.getGenres().get(0).getName(), "Ужосы");


    }

    @Test
    public void enrichMoviesWithGenresTest() throws Exception {

        List <Movie> actualMovies = createMovieList();

        GenreDao mockGenreDao = mock(GenreDao.class);
        when(mockGenreDao.getAllGenres()).thenReturn(actualGenres);

        GenreServiceImpl genreService = new GenreServiceImpl(mockGenreDao);

        assertNull(actualMovies.get(0).getGenres().get(0).getName());

        genreService.enrichMoviesWithGenres(actualMovies);

        assertEquals(actualMovies.get(0).getGenres().get(0).getName(), "Ужосы");


    }

    private List<Genre> createGenreList() {
        List<Genre> genres = new ArrayList<>();
        Genre genre = new Genre();
        genre.setId(1);
        genre.setName("Ужосы");
        genres.add(genre);
        return genres;
    }

    private List<Movie> createMovieList() {
        List<Genre> genres = new ArrayList<>();
        Genre genre = new Genre();
        genre.setId(1);
        genres.add(genre);
        List<Movie> movies = new ArrayList<>();
        Movie movie = new Movie();
        movie.setMovieId(1);
        movie.setNameRussian("Test russian name");
        movie.setNameNative("Test native name");
        movie.setDescription("Test Description");
        movie.setYearOfRelease(3000);
        movie.setPrice(123.12);
        movie.setRating(1.1);
        movie.setPicturePath("Test path");
        movie.setGenres(genres);
        movies.add(movie);
        return movies;
    }


}
