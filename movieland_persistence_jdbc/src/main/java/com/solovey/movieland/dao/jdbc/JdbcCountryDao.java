package com.solovey.movieland.dao.jdbc;

import com.solovey.movieland.dao.CountryDao;
import com.solovey.movieland.dao.jdbc.mapper.CountryRowMapper;
import com.solovey.movieland.entity.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class JdbcCountryDao implements CountryDao {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private static final CountryRowMapper COUNTRY_ROW_MAPPER = new CountryRowMapper();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private String getAllCountriesSql;

    @Autowired
    private String insertCountryMappingSql;

    @Autowired
    private String deleteCountryMappingsByIdsSql;

    @Override
    public List<Country> getAll() {
        log.info("Start query to get all countries from DB");
        long startTime = System.currentTimeMillis();

        List<Country> countries = jdbcTemplate.query(getAllCountriesSql, COUNTRY_ROW_MAPPER);
        log.info("Finish query to get all countries from DB. It took {} ms", System.currentTimeMillis() - startTime);
        return countries;
    }

    @Override
    public void addCountryMapping(List<Country> countries, int movieId) {
        log.info("Start inserting countries {} into country_mappring", countries);
        long startTime = System.currentTimeMillis();
        jdbcTemplate.batchUpdate(insertCountryMappingSql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Country country = countries.get(i);
                ps.setInt(1, movieId);
                ps.setInt(2, country.getId());
            }

            @Override
            public int getBatchSize() {
                return countries.size();
            }
        });

        log.info("Finish inserting country mappings into DB. It took {} ms", System.currentTimeMillis() - startTime);

    }

    @Override
    public void removeCountryMappingsByIds(List<Country> countries, int movieId) {
        log.info("Start deleting countries {} form country_mappring", countries);
        long startTime = System.currentTimeMillis();
        List<Integer> countryIds = new ArrayList<>();
        for (Country country : countries) {
            countryIds.add(country.getId());
        }
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("movieId", movieId)
                .addValue("countryIds", countryIds);
        namedParameterJdbcTemplate.update(deleteCountryMappingsByIdsSql, parameters);
        log.info("Finish deleting country mappings from DB. It took {} ms", System.currentTimeMillis() - startTime);


    }


}


