package com.solovey.movieland.entity;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Rating {
    private AtomicLong rating;
    private AtomicInteger ratesCount;

    public Rating(double rating, int ratesCount) {
        this.rating = new AtomicLong(Double.doubleToLongBits(rating));
        this.ratesCount = new AtomicInteger(ratesCount);
    }

    public AtomicLong getRating() {
        return rating;
    }

    public void setRating(AtomicLong rating) {
        this.rating = rating;
    }

    public AtomicInteger getRatesCount() {
        return ratesCount;
    }

    public void setRatesCount(AtomicInteger ratesCount) {
        this.ratesCount = ratesCount;
    }
}
