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


@Service
public class MovieRatingServiceImpl implements MovieRatingService {

    private final MovieRatingDao movieRatingDao;

    private Set<UserMovieRate> movieRateBuffer = ConcurrentHashMap.newKeySet();

    private Map<Integer, Rating> moviesRatingCache;

    @Autowired
    public MovieRatingServiceImpl(MovieRatingDao movieRatingDao) {
        this.movieRatingDao = movieRatingDao;
    }


    @Override
    public void rateMovie(int userId, int movieId, double rate) {
        UserMovieRate userMovieRate = new UserMovieRate();
        userMovieRate.setMovieId(movieId);
        userMovieRate.setRating(rate);
        userMovieRate.setUserId(userId);

        if (!isMovieRatedByUser(userMovieRate) && movieRatingDao.getUserMovieRateCount(userId, movieId) == 0) {
            movieRateBuffer.add(userMovieRate);
            calculateMovieRating(userMovieRate);
        } else {
            throw new RuntimeException("You have already rated this movie");
        }
    }

    @Override
    public Optional<Double> getMovieRating(int movieId) {
        Rating rating = moviesRatingCache.get(movieId);
        if (rating != null) {
            return Optional.of(rating.getRating());
        }
        return Optional.empty();
    }

    private void calculateMovieRating(UserMovieRate userMovieRate) {
        //atomic
        moviesRatingCache.compute(userMovieRate.getMovieId(), (k, v) -> {
            if (v == null) {
                return new Rating(userMovieRate.getRating(), 1);
            } else {
                int ratesCount = v.getRatesCount();
                double rating = new BigDecimal((v.getRating() * ratesCount + userMovieRate.getRating()) / (ratesCount + 1)).setScale(1, RoundingMode.UP).doubleValue();

                v.setRating(rating);
                v.setRatesCount(ratesCount + 1);
                return v;
            }
        });

    }

    private boolean isMovieRatedByUser(UserMovieRate userMovieRate) {
        for (UserMovieRate bufferedUserMovieRate : movieRateBuffer) {
            if (userMovieRate.getMovieId() == bufferedUserMovieRate.getMovieId() &
                    userMovieRate.getUserId() == bufferedUserMovieRate.getUserId()) {
                return true;
            }
        }
        return false;
    }


    @Scheduled(fixedDelayString = "${rates.flush.interval}", initialDelayString = "${rates.flush.interval}")
    private void flushRatesToPersistence() {
        List<UserMovieRate> rates = new ArrayList<>();
        Iterator<UserMovieRate> iterator = movieRateBuffer.iterator();
        while (iterator.hasNext()) {
            rates.add(iterator.next());
            iterator.remove();
        }
        if (rates.size() > 0) {
            movieRatingDao.flushRatesToPersistence(rates);
        }
    }

    @PostConstruct
    private void invalidate() {
        moviesRatingCache = movieRatingDao.getMoviesRatings();
    }


}