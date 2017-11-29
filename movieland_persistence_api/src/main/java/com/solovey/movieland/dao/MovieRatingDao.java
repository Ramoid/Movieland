package com.solovey.movieland.dao;


import com.solovey.movieland.entity.Rating;
import com.solovey.movieland.entity.UserMovieRate;

import java.util.List;
import java.util.Map;


public interface MovieRatingDao {
    void flushRatesToPersistence(List<UserMovieRate> rates);
    Map<Integer,Rating> getMoviesRatings();
    int getUserMovieRateCount(int userId, int movieId);
}
