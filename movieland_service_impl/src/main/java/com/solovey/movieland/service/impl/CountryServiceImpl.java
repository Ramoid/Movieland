package com.solovey.movieland.service.impl;


import com.solovey.movieland.dao.CountryDao;
import com.solovey.movieland.entity.Country;
import com.solovey.movieland.entity.Movie;
import com.solovey.movieland.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CountryServiceImpl implements CountryService {

    private final CountryDao countryDao;

    @Autowired
    public CountryServiceImpl(CountryDao countryDao) {
        this.countryDao = countryDao;
    }

    @Override
    public List<Country> getAllGCountries() {
        return countryDao.getAllCountries();
    }

    @Override
    public void enrichMoviesWithCountries(List<Movie> movies) {
        Map<Integer, String> countriesMap = countryDao.getAllCountries().stream().collect(Collectors.toMap(x -> x.getId(), x -> x.getName()));

        for (Movie movie : movies) {
            for (Country movieCountry : movie.getCountries()) {
                movieCountry.setName(countriesMap.get(movieCountry.getId()));
            }
        }
    }

    @Override
    public void enrichMovieWithCountries(Movie movie) {
        Map<Integer, String> countriesMap = countryDao.getAllCountries().stream().collect(Collectors.toMap(x -> x.getId(), x -> x.getName()));

        for (Country movieCountry : movie.getCountries()) {
            movieCountry.setName(countriesMap.get(movieCountry.getId()));
        }
    }

}