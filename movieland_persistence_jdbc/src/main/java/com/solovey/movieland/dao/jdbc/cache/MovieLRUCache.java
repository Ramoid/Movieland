package com.solovey.movieland.dao.jdbc.cache;


import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.solovey.movieland.dao.MovieDao;
import com.solovey.movieland.dao.enums.SortDirection;
import com.solovey.movieland.dao.jdbc.JdbcMovieDao;
import com.solovey.movieland.entity.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

@Service
@Primary
@Profile("lru.cache")
public class MovieLRUCache implements MovieDao {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Value("${lru.cache.capacity}")
    private int cacheCapacity;


    private ConcurrentMap<Integer, Movie> movieCache;

    @Autowired
    @Qualifier("jdbcMovieDao")
    private MovieDao movieDao;

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
        log.info("Trying to retrieve movie with id {} from the LRU cache ", movieId);
        long startTime = System.currentTimeMillis();

        Movie movie;

        movie = movieCache.get(movieId);
        if (movie != null) {
            log.info("Movie was retriewed from the LRU cache It took {} ms", System.currentTimeMillis() - startTime);
            return movie;
        }

        log.info("Movie was not found in cache. Retriewing from db... ");
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

    @PostConstruct
    private  void initCache(){
        movieCache = new ConcurrentLinkedHashMap.Builder<Integer, Movie>()
                .maximumWeightedCapacity(cacheCapacity)
                .build();
    }

    private void addMovieToCache(Movie movie) {
        log.info("Add movie with id {} to the LRU cache ", movie.getMovieId());
        long startTime = System.currentTimeMillis();
        movieCache.put(movie.getMovieId(), movie);
        log.info("Movie has been added to the LRU cache. It took {} ms", System.currentTimeMillis() - startTime);
    }
}
