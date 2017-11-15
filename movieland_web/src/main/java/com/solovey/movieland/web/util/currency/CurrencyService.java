package com.solovey.movieland.web.util.currency;


import com.solovey.movieland.entity.Movie;
import com.solovey.movieland.entity.enums.Currency;
import com.solovey.movieland.web.util.currency.cache.CurrencyCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;


@Service
public class CurrencyService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final CurrencyCache currencyCache;

    @Autowired
    public CurrencyService(CurrencyCache currencyCache) {
        this.currencyCache = currencyCache;
    }


    public double getCurrencyRate(Currency currency) {
        log.info("Start getting CurrencyRate for {}", currency.getCurrency());
        long startTime = System.currentTimeMillis();
        double rate = currencyCache.getCurencyRate(currency);
        log.info("Currency Rate for {} is received = {}. It took {} ms", currency.getCurrency(), rate, System.currentTimeMillis() - startTime);
        return rate;
    }


    public void convertMoviePrice(Movie movie, double currencyRate) {
        double price = new BigDecimal(movie.getPrice() / currencyRate).setScale(2, RoundingMode.UP).doubleValue();
        movie.setPrice(price);
    }
}
