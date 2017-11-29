package com.solovey.movieland.entity;

public class Rating {
    private double rating;
    private int ratesCount;

    public Rating(double rating, int ratesCount) {
        this.rating = rating;
        this.ratesCount = ratesCount;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getRatesCount() {
        return ratesCount;
    }

    public void setRatesCount(int ratesCount) {
        this.ratesCount = ratesCount;
    }
}
