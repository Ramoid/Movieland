package com.solovey.movieland.web.util.currency;


import com.solovey.movieland.entity.Movie;
import com.solovey.movieland.entity.enums.Currency;
import com.solovey.movieland.web.util.currency.cache.CurrencyCache;
import com.solovey.movieland.web.util.json.JsonJacksonConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLConnection;
import java.security.Security;
import java.util.Map;

@Service
public class CurrencyService {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private JsonJacksonConverter jsonConverter;
    @Value("${url}")
    private String urlText;

    @Autowired
    CurrencyCache currencyCache;

    @Autowired
    public CurrencyService(JsonJacksonConverter jsonJacksonConverter) {
        this.jsonConverter = jsonJacksonConverter;
    }

    @PostConstruct
    public void init() {
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
    }

    public double getCurrencyRate(Currency currency) {
        log.info("Start getting CurrencyRate for {}", currency.getCurrency());
        long startTime = System.currentTimeMillis();
        double rate = currencyCache.getCurencyRate(currency);
        log.info("Currency Rate for {} is received = {}. It took {} ms", currency.getCurrency(), rate, System.currentTimeMillis() - startTime);
        return rate;
    }

    public void getCurrencyRates(Map<Currency, Double> currencyRateMap) {
        log.info("Start getting CurrencyRates url {}", urlText);
        long startTime = System.currentTimeMillis();
        double currencyRate;

        try {
            URL url = new URL(urlText);
            URLConnection conn = url.openConnection();
            Map<Currency, Double> ratesMap = jsonConverter.extractCurrencyRates(conn.getInputStream(), currencyRateMap);
            log.info("Currency Rates is received. It took {} ms", System.currentTimeMillis() - startTime);

        } catch (IOException e) {
            log.error("Error receiving currency rates for URL {}  with error {}", urlText, e);
            throw new RuntimeException(e);
        }

    }


    public void convertMoviePrice(Movie movie, double currencyRate) {
        double price = new BigDecimal(movie.getPrice() / currencyRate).setScale(2, RoundingMode.UP).doubleValue();
        movie.setPrice(price);
    }
}
