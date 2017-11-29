package com.solovey.movieland.service;


import java.util.Optional;

public interface MovieRatingService {
    void rateMovie(int userId, int movieId, double rate);
    Optional<Double> getMovieRating(int movieId);

}
