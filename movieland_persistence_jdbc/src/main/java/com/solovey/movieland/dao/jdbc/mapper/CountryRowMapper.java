package com.solovey.movieland.dao.jdbc.mapper;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CountryRowMapper implements RowMapper<com.solovey.movieland.entity.Country> {
    @Override
    public com.solovey.movieland.entity.Country mapRow(ResultSet resultSet, int i) throws SQLException {
        com.solovey.movieland.entity.Country country = new com.solovey.movieland.entity.Country();
        country.setId(resultSet.getInt("country_id"));
        country.setName(resultSet.getString("country"));
        return country;
    }


}
