package com.solovey.movieland.dao.jdbc.mapper;

import com.solovey.movieland.entity.reporting.ReportTopUser;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportTopUserRowMapper implements RowMapper<ReportTopUser> {

    @Override
    public ReportTopUser mapRow(ResultSet resultSet, int i) throws SQLException {
        ReportTopUser user = new ReportTopUser();
        user.setUserId(resultSet.getInt("user_id"));
        user.setEmail(resultSet.getString("email"));
        user.setReviewsCount(resultSet.getInt("reviews_count"));
        user.setAverageRate(resultSet.getDouble("avg_rating"));

        return user;
    }
}
