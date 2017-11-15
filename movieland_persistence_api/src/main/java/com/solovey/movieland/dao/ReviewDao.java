package com.solovey.movieland.dao;


import com.solovey.movieland.entity.Review;

import java.util.List;

public interface ReviewDao {
    List<Review> getReviewsByMovieId(int movieId);
    void saveReviewToDb(Review review);
}
