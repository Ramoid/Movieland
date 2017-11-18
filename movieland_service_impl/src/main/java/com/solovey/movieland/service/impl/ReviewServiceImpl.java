package com.solovey.movieland.service.impl;


import com.solovey.movieland.dao.ReviewDao;
import com.solovey.movieland.entity.Movie;
import com.solovey.movieland.entity.Review;
import com.solovey.movieland.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewDao reviewDao;

    @Override
    public void enrichMovieWithReviews(Movie movie) {
        movie.setReviews(reviewDao.getReviewsByMovieId(movie.getMovieId()));
    }

    @Override
    public Review addReview(Review review) {
        return reviewDao.addReview(review);
    }
}