package com.solovey.movieland.dao;


import java.util.Set;

public interface MovieRateDao {
    void rateMovie(int userId, int movieId, double rate);
    void updateMoviesRating(Set<Integer> movies);
}
