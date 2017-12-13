package com.solovey.movieland.dao.jdbc.mapper;

import com.solovey.movieland.entity.reporting.ReportMovie;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportMovieRowMapper implements RowMapper<ReportMovie> {

    @Override
    public ReportMovie mapRow(ResultSet resultSet, int i) throws SQLException {
        ReportMovie movie = new ReportMovie();
        movie.setMovieId(resultSet.getInt("movie_id"));
        movie.setTitle(resultSet.getString("title"));
        movie.setDescription(resultSet.getString("description"));
        movie.setGenre(resultSet.getString("genre"));
        movie.setPrice(resultSet.getDouble("price"));
        movie.setAddedDate(resultSet.getDate("added_date").toLocalDate());
        movie.setModifiedDate(resultSet.getDate("modified_date").toLocalDate());
        movie.setRating(resultSet.getDouble("rating"));
        movie.setReviewsCount(resultSet.getInt("reviews_count"));
        return movie;
    }
}
