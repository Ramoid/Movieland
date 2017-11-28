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
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private MovieParallelEnrichmentService movieParallelEnrichmentService;

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
        movieParallelEnrichmentService.enrichMovie(movie);
        return movie;
    }

    @Override
    @Transactional
    public Movie addMovie(Movie movie) {
        Movie movieToReturn = movieDao.addMovie(movie);
        genreService.addGenreMapping(movieToReturn.getGenres(), movieToReturn.getMovieId());
        countryService.addCountryMapping(movieToReturn.getCountries(), movieToReturn.getMovieId());
        return movieToReturn;
    }

    @Override
    @Transactional
    public void editMovie(Movie movie) {

        movieDao.editMovie(movie);
        if (movie.getGenres() != null) {
            genreService.removeGenreMappingsByIds(movie.getGenres(), movie.getMovieId());
            genreService.addGenreMapping(movie.getGenres(), movie.getMovieId());
        }
        if (movie.getCountries() != null) {
            countryService.removeCountryMappingsByIds(movie.getCountries(), movie.getMovieId());
            countryService.addCountryMapping(movie.getCountries(), movie.getMovieId());
        }
    }


}
