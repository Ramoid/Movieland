package com.solovey.movieland.service;

import com.solovey.movieland.entity.Genre;
import com.solovey.movieland.entity.Movie;

import java.util.List;

public interface GenreService {
    List<Genre> getAll();

    void enrichMoviesWithGenres(List<Movie> movies);

    void enrichMovieWithGenres(Movie movie);
}
