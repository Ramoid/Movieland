package com.solovey.movieland.service;

import com.solovey.movieland.entity.Movie;
import com.solovey.movieland.entity.Review;


public interface ReviewService {
    void enrichMovieWithReviews(Movie movie);
    Review addReview(Review review);
}
