package com.solovey.movieland.dao.jdbc.cache;


import com.solovey.movieland.dao.MovieDao;
import com.solovey.movieland.dao.enums.SortDirection;
import com.solovey.movieland.entity.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Primary
@Profile("SoftReferenceCache")
public class MovieSoftReferenceCache implements MovieDao {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private ConcurrentHashMap<Integer, Reference<Movie>> movieCache = new ConcurrentHashMap<>();

    private MovieDao movieDao;

    @Autowired
    public MovieSoftReferenceCache(MovieDao movieDao) {
        this.movieDao = movieDao;
    }

    @Override
    public List<Movie> getAllMovies(Map<String, SortDirection> sortType) {
        return movieDao.getAllMovies(sortType);
    }

    @Override
    public List<Movie> getRandomMovies() {
        return movieDao.getRandomMovies();
    }

    @Override
    public List<Movie> getMoviesByGenreId(int id, Map<String, SortDirection> sortType) {
        return movieDao.getMoviesByGenreId(id, sortType);
    }

    @Override
    public Movie getMovieById(int movieId) {
        log.info("Trying to retrive movie with id {} from the soft reference cache ",movieId);
        long startTime = System.currentTimeMillis();

        Reference<Movie> movieReference = movieCache.get(movieId);
        Movie movie;

        if (movieReference != null) {

            movie = movieReference.get();
            if (movie != null) {
                log.info("Movie was retriewed from the soft reference cache It took {} ms", System.currentTimeMillis() - startTime);
                return movie;
            }
        }
        log.info("Movie was not found in a cache. Retriewing from db... ");
        movie = movieDao.getMovieById(movieId);
        addMovieToCache(movie);
        return movie;

    }

    @Override
    public Movie addMovie(Movie movie) {
        Movie addedMovie = movieDao.addMovie(movie);
        addMovieToCache(addedMovie);
        return addedMovie;
    }

    @Override
    public void editMovie(Movie movie) {
        movieDao.editMovie(movie);
        addMovieToCache(movie);

    }

    private void addMovieToCache(Movie movie) {
        log.info("Add movie with id {} to the soft reference cache ", movie.getMovieId());
        long startTime = System.currentTimeMillis();
        SoftReference<Movie> movieSoftReference = new SoftReference<Movie>(movie);
        movieCache.put(movie.getMovieId(), movieSoftReference);
        log.info("Movie has been added to the soft reference cache. It took {} ms", System.currentTimeMillis() - startTime);
    }
}
