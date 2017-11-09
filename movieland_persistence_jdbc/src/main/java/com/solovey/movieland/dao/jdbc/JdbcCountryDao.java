package com.solovey.movieland.dao.jdbc;

import com.solovey.movieland.dao.CountryDao;
import com.solovey.movieland.dao.jdbc.mapper.CountryRowMapper;
import com.solovey.movieland.entity.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JdbcCountryDao implements CountryDao {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private static final CountryRowMapper COUNTRY_ROW_MAPPER = new CountryRowMapper();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private String getAllCountriesSql;

    @Override
    public List<Country> getAllCountries() {
        log.info("Start query to get all countries from DB");
        long startTime = System.currentTimeMillis();

        List<Country> countries = jdbcTemplate.query(getAllCountriesSql, COUNTRY_ROW_MAPPER);
        log.info("Finish query to get all countries from DB. It took {} ms", System.currentTimeMillis() - startTime);
        return countries;
    }


}


