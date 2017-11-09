package com.solovey.movieland.dao.jdbc.mapper;

import com.solovey.movieland.entity.Review;
import com.solovey.movieland.entity.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReviewRowMapper implements RowMapper<Review> {
    @Override
    public Review mapRow(ResultSet resultSet, int i) throws SQLException {
        Review review = new Review();
        User user= new User();
        user.setId(resultSet.getInt("user_id"));
        user.setNickname(resultSet.getString("user_name"));
        review.setId(resultSet.getInt("review_id"));
        review.setUser(user);
        review.setText(resultSet.getString("rtext"));
        return review;
    }

}
