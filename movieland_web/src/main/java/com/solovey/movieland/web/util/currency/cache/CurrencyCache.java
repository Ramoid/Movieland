package com.solovey.movieland.web.util.currency.cache;

import com.solovey.movieland.entity.enums.Currency;
import com.solovey.movieland.web.util.currency.CurrencyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Primary
public class CurrencyCache  {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private Map<Currency,Double> currencyRateCache = new ConcurrentHashMap<>();

    private CurrencyService currencyService;

    @Autowired
    public CurrencyCache(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    public double getCurencyRate(Currency currency) {
        return currencyRateCache.get(currency);
    }

    @PostConstruct
    @Scheduled(cron = "${cron}")
    private void invalidate() {
        log.info("Start currency cache refresh");
        long startTime = System.currentTimeMillis();
        currencyService.getCurrencyRates(currencyRateCache);
        log.info("Currency chache has been reloaded {}. It took {} ms",currencyRateCache.toString(), System.currentTimeMillis() - startTime);
    }

}
