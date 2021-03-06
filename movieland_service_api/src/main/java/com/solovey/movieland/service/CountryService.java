package com.solovey.movieland.service;


import com.solovey.movieland.entity.Country;
import com.solovey.movieland.entity.Movie;

import java.util.List;

public interface CountryService {
    List<Country> getAll();

    void enrichMoviesWithCountries(List<Movie> movies);

    void enrichMovieWithCountries(Movie movie);

    void addCountryMapping(List<Country> countries, int movieId);

    void removeCountryMappingsByIds(List<Country> countries, int movieId);
}
