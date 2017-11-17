package com.solovey.movieland.service.impl;


import com.solovey.movieland.dao.GenreDao;
import com.solovey.movieland.entity.Genre;
import com.solovey.movieland.entity.Movie;
import com.solovey.movieland.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreDao genreDao;

    @Autowired
    public GenreServiceImpl(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    @Override
    public List<Genre> getAll() {
        return genreDao.getAll();
    }

    @Override
    public void enrichMoviesWithGenres(List<Movie> movies) {
        Map<Integer, String> genresMap = genreDao.getAll().stream().collect(Collectors.toMap(x -> x.getId(), x -> x.getName()));

        for (Movie movie : movies) {
            for (Genre movieGenre : movie.getGenres()) {
                movieGenre.setName(genresMap.get(movieGenre.getId()));
            }
        }
    }

    @Override
    public void enrichMovieWithGenres(Movie movie) {
        Map<Integer, String> genresMap = genreDao.getAll().stream().collect(Collectors.toMap(x -> x.getId(), x -> x.getName()));

        for (Genre movieGenre : movie.getGenres()) {
            movieGenre.setName(genresMap.get(movieGenre.getId()));
        }

    }

}