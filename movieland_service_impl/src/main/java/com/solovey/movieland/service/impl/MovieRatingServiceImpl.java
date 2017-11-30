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
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


@Service
public class MovieRatingServiceImpl implements MovieRatingService {

    private final MovieRatingDao movieRatingDao;

    private Queue<UserMovieRate> movieRateBuffer = new ConcurrentLinkedDeque<>();

    private Map<Integer, Rating> moviesRatingCache = new ConcurrentHashMap<>();

    @Autowired
    public MovieRatingServiceImpl(MovieRatingDao movieRatingDao) {
        this.movieRatingDao = movieRatingDao;
    }

    private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
    private Lock readLock = reentrantReadWriteLock.readLock();
    private Lock writeLock = reentrantReadWriteLock.writeLock();


    @Override
    public void rateMovie(UserMovieRate userMovieRate) {

        if (!isMovieRatedByUser(userMovieRate)) {
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
            return Optional.of(Double.longBitsToDouble(rating.getRating().get()));
        }
        return Optional.empty();
    }

    private void calculateMovieRating(UserMovieRate userMovieRate) {
        boolean[] wasAbsent = {false};
        moviesRatingCache.computeIfAbsent(userMovieRate.getMovieId(), v -> {
            wasAbsent[0] = true;
            return new Rating(userMovieRate.getRating(), 1);
        });

        if (!wasAbsent[0]) {
            Rating rating = moviesRatingCache.get(userMovieRate.getMovieId());
            rating.getRating().updateAndGet(x -> {
                int ratesCount = rating.getRatesCount().incrementAndGet();
                return Double.doubleToLongBits(new BigDecimal((Double.longBitsToDouble(x) * (ratesCount - 1) + userMovieRate.getRating()) / ratesCount).setScale(1, RoundingMode.UP).doubleValue());
            });

        }

    }

    private boolean isMovieRatedByUser(UserMovieRate userMovieRate) {
        readLock.lock();
        try {
            for (UserMovieRate bufferedUserMovieRate : movieRateBuffer) {
                if (userMovieRate.getMovieId() == bufferedUserMovieRate.getMovieId() &&
                        userMovieRate.getUserId() == bufferedUserMovieRate.getUserId()) {
                    return true;
                }
            }
        } finally {
            readLock.unlock();
        }
        return false;
    }


    @Scheduled(fixedDelayString = "${rates.flush.interval}", initialDelayString = "${rates.flush.interval}")
    private void flushRatesToPersistence() {
        writeLock.lock();
        try {
            if (!movieRateBuffer.isEmpty()) {
                List<UserMovieRate> rates = new ArrayList<>();
                Iterator<UserMovieRate> iterator = movieRateBuffer.iterator();
                while (iterator.hasNext()) {
                    rates.add(iterator.next());
                    iterator.remove();
                }
                movieRatingDao.flushRatesToPersistence(rates);
            }
        } finally {
            writeLock.unlock();
        }
    }

    @PostConstruct
    private void invalidate() {
        moviesRatingCache.putAll(movieRatingDao.getMoviesRatings());
    }


}