package com.solovey.movieland.entity;


public class UserMovieRate {
    private int userId;
    private int movieId;
    private double rating;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }


    @Override
    public int hashCode() {
        int result;
        long temp;
        result = userId;
        result = 31 * result + movieId;
        temp = Double.doubleToLongBits(rating);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserMovieRate that = (UserMovieRate) o;

        if (userId != that.userId) return false;
        if (movieId != that.movieId) return false;
        return Double.compare(that.rating, rating) == 0;
    }

    @Override
    public String toString() {
        return "UserMovieRate{" +
                "userId=" + userId +
                ", movieId=" + movieId +
                ", rating=" + rating +
                '}';
    }
}
