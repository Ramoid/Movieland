package com.solovey.movieland.service.impl;

import com.solovey.movieland.dao.MovieDao;
import com.solovey.movieland.entity.Movie;
import com.solovey.movieland.dao.enums.SortDirection;
import com.solovey.movieland.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    private MovieRatingService movieRatingService;

    @Override
    public List<Movie> getAllMovies(Map<String, SortDirection> sortType) {

        List<Movie> movies = movieDao.getAllMovies(sortType);
        for (Movie movie : movies) {
            setMovieRating(movie);
        }
        return movies;
    }

    @Override
    public List<Movie> getRandomMovies() {
        List<Movie> movies = movieDao.getRandomMovies();
        for (Movie movie : movies) {
            setMovieRating(movie);
        }
        genreService.enrichMoviesWithGenres(movies);
        countryService.enrichMoviesWithCountries(movies);
        return movies;
    }

    @Override
    public List<Movie> getMoviesByGenreId(int id, Map<String, SortDirection> sortType) {
        List<Movie> movies = movieDao.getMoviesByGenreId(id, sortType);
        for (Movie movie : movies) {
            setMovieRating(movie);
        }
        return movies;
    }

    @Override
    public Movie getMovieById(int movieId) {

        Movie movie = movieDao.getMovieById(movieId);
        setMovieRating(movie);
        genreService.enrichMovieWithGenres(movie);
        countryService.enrichMovieWithCountries(movie);
        reviewService.enrichMovieWithReviews(movie);

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

    private void setMovieRating(Movie movie) {
        Optional<Double> rating = movieRatingService.getMovieRating(movie.getMovieId());
        rating.ifPresent(movie::setRating);

    }

}
