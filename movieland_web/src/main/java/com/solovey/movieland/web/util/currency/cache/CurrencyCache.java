package com.solovey.movieland.web.util.currency.cache;

import com.solovey.movieland.entity.enums.Currency;
import com.solovey.movieland.web.util.json.JsonJacksonConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.security.Security;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Primary
public class CurrencyCache {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private Map<Currency, Double> currencyRateCache = new ConcurrentHashMap<>();

    @Value("${currency_rate_api_url}")
    private String currencyApiUrl;

    private JsonJacksonConverter jsonConverter;


    @Autowired
    public CurrencyCache(JsonJacksonConverter jsonConverter) {
        this.jsonConverter = jsonConverter;
    }

    public double getCurencyRate(Currency currency) {
        return currencyRateCache.get(currency);
    }

    @PostConstruct
    private void init() {
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
        invalidate();
    }

    @Scheduled(cron = "${current_cache_cron_config}")
    private void invalidate() {
        log.info("Start currency cache refresh");
        long startTime = System.currentTimeMillis();
        reloadCurrencyRates();
        log.info("Currency chache has been reloaded {}. It took {} ms", currencyRateCache, System.currentTimeMillis() - startTime);
    }

    private void reloadCurrencyRates() {
        log.info("Start getting CurrencyRates url {}", currencyApiUrl);
        long startTime = System.currentTimeMillis();

        try {
            URL url = new URL(currencyApiUrl);
            URLConnection conn = url.openConnection();
            jsonConverter.extractCurrencyRates(conn.getInputStream(), currencyRateCache);
            log.info("Currency Rates is received. It took {} ms", System.currentTimeMillis() - startTime);

        } catch (IOException e) {
            log.error("Error receiving currency rates for URL {}  with error {}", currencyApiUrl, e);
            throw new RuntimeException(e);
        }

    }

}
