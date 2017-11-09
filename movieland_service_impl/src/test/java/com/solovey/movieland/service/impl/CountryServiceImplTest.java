package com.solovey.movieland.service.impl;


import com.solovey.movieland.dao.CountryDao;
import com.solovey.movieland.entity.Country;
import com.solovey.movieland.entity.Movie;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class CountryServiceImplTest {

    List<Country> actualCountries;

    @Before
    public void before() {
        actualCountries = createCountryList();
    }

    @Test
    public void enrichMovieWithCountriesTest() throws Exception {

        Movie actualMovie = createMovieList().get(0);

        CountryDao mockCountryDao = mock(CountryDao.class);
        when(mockCountryDao.getAllCountries()).thenReturn(actualCountries);

        CountryServiceImpl countryService = new CountryServiceImpl(mockCountryDao);

        assertNull(actualMovie.getCountries().get(0).getName());

        countryService.enrichMovieWithCountries(actualMovie);

        assertEquals(actualMovie.getCountries().get(0).getName(), "Country");


    }

    @Test
    public void enrichMoviesWithGenresTest() throws Exception {

        List <Movie> actualMovies = createMovieList();


        CountryDao mockCountryDao = mock(CountryDao.class);
        when(mockCountryDao.getAllCountries()).thenReturn(actualCountries);

        CountryServiceImpl countryService = new CountryServiceImpl(mockCountryDao);

        assertNull(actualMovies.get(0).getCountries().get(0).getName());

        countryService.enrichMoviesWithCountries(actualMovies);

        assertEquals(actualMovies.get(0).getCountries().get(0).getName(), "Country");


    }

    private List<Country> createCountryList() {
        List<Country> countries = new ArrayList<>();
        Country country = new Country();
        country.setId(1);
        country.setName("Country");
        countries.add(country);
        return countries;
    }

    private List<Movie> createMovieList() {
        List<Country> countries = new ArrayList<>();
        Country country = new Country();
        country.setId(1);
        countries.add(country);
        List<Movie> movies = new ArrayList<Movie>();
        Movie movie = new Movie();
        movie.setMovieId(1);
        movie.setNameRussian("Test russian name");
        movie.setNameNative("Test native name");
        movie.setDescription("Test Description");
        movie.setYearOfRelease(3000);
        movie.setPrice(123.12);
        movie.setRating(1.1);
        movie.setPicturePath("Test path");
        movie.setCountries(countries);
        movies.add(movie);
        return movies;
    }


}
