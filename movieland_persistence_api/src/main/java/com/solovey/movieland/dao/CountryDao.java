package com.solovey.movieland.dao;

import com.solovey.movieland.entity.Country;
import com.solovey.movieland.entity.Movie;

import java.util.List;

public interface CountryDao {
   List<Country> getAllCountries();
}
