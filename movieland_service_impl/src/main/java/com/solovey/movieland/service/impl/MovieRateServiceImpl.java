package com.solovey.movieland.service.impl;


import com.solovey.movieland.dao.MovieRateDao;
import com.solovey.movieland.service.MovieRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class MovieRateServiceImpl implements MovieRateService {

    private final MovieRateDao movieRateDao;

    private Set<Integer> moviesBuffer = ConcurrentHashMap.newKeySet();

    @Autowired
    public MovieRateServiceImpl(MovieRateDao movieRateDao) {
        this.movieRateDao = movieRateDao;
    }

    @Override
    public void rateMovie(int userId, int movieId, double rate) {
        movieRateDao.rateMovie(userId,movieId,rate);
        addToBuffer(movieId);
    }

    public boolean isMovieInBuffer(int movieId){
        return moviesBuffer.contains(movieId);
    }

    private void addToBuffer(Integer movieId){
        moviesBuffer.add(movieId);
    }

    private Set<Integer> getMoviesFromBuffer(){
        Set<Integer> movies=new HashSet<>();
        Iterator<Integer> iterator = moviesBuffer.iterator();
        while(iterator.hasNext()) {
            movies.add(iterator.next());
            iterator.remove();
        }
        return movies;
    }

    @Scheduled(fixedDelayString = "${rating.calculation.interval}", initialDelayString = "${rating.calculation.interval}")
    private void calculareMoviesRating(){
        Set<Integer> movies=getMoviesFromBuffer();
        if(movies.size()>0){
            movieRateDao.updateMoviesRating(movies);
        }
    }
}