package com.solovey.movieland.service.impl;


import com.solovey.movieland.dao.ReviewDao;
import com.solovey.movieland.entity.Movie;
import com.solovey.movieland.entity.Review;
import com.solovey.movieland.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ReviewServiceImpl implements ReviewService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ReviewDao reviewDao;

    @Override
    public void enrichMovieWithReviews(Movie movie) {
        List<Review> reviewList = reviewDao.getReviewsByMovieId(movie.getMovieId());
        if (Thread.currentThread().isInterrupted()) {
            log.info("enrichMovieWithReviews was interrupted due to timeout");
        } else {
            movie.setReviews(reviewList);
        }
    }

    @Override
    public Review addReview(Review review) {
        return reviewDao.addReview(review);
    }
}