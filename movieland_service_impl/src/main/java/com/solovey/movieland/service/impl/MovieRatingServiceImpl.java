package com.solovey.movieland.service.impl;


import com.solovey.movieland.dao.MovieRatingDao;
import com.solovey.movieland.entity.UserMovieRate;
import com.solovey.movieland.service.MovieRatingService;
import com.solovey.movieland.entity.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;


@Service
public class MovieRatingServiceImpl implements MovieRatingService {

    private final MovieRatingDao movieRatingDao;

    private Queue<UserMovieRate> movieRateBuffer = new ConcurrentLinkedQueue<>();

    private Map<Integer, Rating> moviesRatingCache = new ConcurrentHashMap<>();

    @Autowired
    public MovieRatingServiceImpl(MovieRatingDao movieRatingDao) {
        this.movieRatingDao = movieRatingDao;
    }


    @Override
    public void rateMovie(UserMovieRate userMovieRate) {

        movieRateBuffer.add(userMovieRate);
        calculateMovieRating(userMovieRate);

    }

    @Override
    public Optional<Double> getMovieRating(int movieId) {
        Rating rating = moviesRatingCache.get(movieId);
        if (rating != null) {
            return Optional.of(Double.longBitsToDouble(rating.getRating().get()));
        }
        return Optional.empty();
    }

    private void calculateMovieRating(UserMovieRate userMovieRate) {

        Rating rating = moviesRatingCache.computeIfAbsent(userMovieRate.getMovieId(), v -> {
            return new Rating(userMovieRate.getRating(), 0);
        });

        rating.getRating().updateAndGet(x -> {
            int ratesCount = rating.getRatesCount().incrementAndGet();
            double newRate = new BigDecimal((Double.longBitsToDouble(x) * (ratesCount - 1) + userMovieRate.getRating()) / ratesCount).setScale(1, RoundingMode.UP).doubleValue();
            return Double.doubleToLongBits(newRate);
        });

    }


    @Scheduled(fixedDelayString = "${rates.flush.interval}", initialDelayString = "${rates.flush.interval}")
    private void flushRatesToPersistence() {

        if (!movieRateBuffer.isEmpty()) {
            List<UserMovieRate> rates = new ArrayList<>();
            Iterator<UserMovieRate> iterator = movieRateBuffer.iterator();
            while (iterator.hasNext()) {
                rates.add(iterator.next());
                iterator.remove();
            }
            movieRatingDao.flushRatesToPersistence(rates);
        }

    }

    @PostConstruct
    private void invalidate() {
        moviesRatingCache.putAll(movieRatingDao.getMoviesRatings());
    }


}