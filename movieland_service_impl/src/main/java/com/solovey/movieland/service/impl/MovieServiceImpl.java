package com.solovey.movieland.service.impl;

import com.solovey.movieland.dao.MovieDao;
import com.solovey.movieland.entity.Movie;
import com.solovey.movieland.dao.enums.SortDirection;
import com.solovey.movieland.service.CountryService;
import com.solovey.movieland.service.GenreService;
import com.solovey.movieland.service.MovieService;
import com.solovey.movieland.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieDao movieDao;

    @Autowired
    private GenreService genreService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private CountryService countryService;

    @Override
    public List<Movie> getAllMovies(Map<String, SortDirection> sortType) {
        return movieDao.getAllMovies(sortType);
    }

    @Override
    public List<Movie> getRandomMovies() {
        List<Movie> movies = movieDao.getRandomMovies();
        genreService.enrichMoviesWithGenres(movies);
        countryService.enrichMoviesWithCountries(movies);
        return movies;
    }

    @Override
    public List<Movie> getMoviesByGenreId(int id, Map<String, SortDirection> sortType) {
        return movieDao.getMoviesByGenreId(id, sortType);
    }

    @Override
    public Movie getMovieById(int movieId) {

        Movie movie = movieDao.getMovieById(movieId);
        genreService.enrichMovieWithGenres(movie);
        countryService.enrichMovieWithCountries(movie);
        reviewService.enrichMovieWithReviews(movie);
        return movie;
    }


}
