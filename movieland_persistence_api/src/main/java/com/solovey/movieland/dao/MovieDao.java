package com.solovey.movieland.dao;

import com.solovey.movieland.entity.Movie;
import com.solovey.movieland.dao.enums.SortDirection;

import java.util.List;
import java.util.Map;

public interface MovieDao {
    List<Movie> getAllMovies(Map<String, SortDirection> sortType);

    List<Movie> getRandomMovies();

    List<Movie> getMoviesByGenreId(int id, Map<String, SortDirection> sortType);

    Movie getMovieById(int movieId);

    Movie addMovie(Movie movie);

    void editMovie(Movie movie);

}
