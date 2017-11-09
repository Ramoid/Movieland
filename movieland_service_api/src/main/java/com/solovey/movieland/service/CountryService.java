package com.solovey.movieland.service;


import com.solovey.movieland.entity.Country;
import com.solovey.movieland.entity.Movie;

import java.util.List;

public interface CountryService {
    List<Country> getAllGCountries();

    void enrichMoviesWithCountries(List<Movie> movies);

    void enrichMovieWithCountries(Movie movie);
}
