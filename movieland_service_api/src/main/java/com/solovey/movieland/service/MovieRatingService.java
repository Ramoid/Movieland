package com.solovey.movieland.service;


import com.solovey.movieland.entity.UserMovieRate;

import java.util.Optional;

public interface MovieRatingService {
    void rateMovie(UserMovieRate userMovieRate);
    Optional<Double> getMovieRating(int movieId);

}
