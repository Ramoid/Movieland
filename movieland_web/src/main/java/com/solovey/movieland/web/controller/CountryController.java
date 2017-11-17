package com.solovey.movieland.web.controller;

import com.solovey.movieland.entity.Country;
import com.solovey.movieland.service.CountryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping("/country")
public class CountryController {
    private final CountryService countryService;
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    public CountryController(CountryService countryService
                           ) {
        this.countryService = countryService;
    }

    @RequestMapping(method = GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public List<Country> getAllCountries() {
        log.info("Sending request to get all countries");
        long startTime = System.currentTimeMillis();
        List<Country> countries = countryService.getAll();
        log.info("Countries are received. It took {} ms", System.currentTimeMillis() - startTime);
        return countries;
    }


}
