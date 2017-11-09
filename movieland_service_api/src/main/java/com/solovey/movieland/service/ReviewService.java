package com.solovey.movieland.service;

import com.solovey.movieland.entity.Movie;


public interface ReviewService {
    void enrichMovieWithReviews(Movie movie);
}
