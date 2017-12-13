package com.solovey.movieland.dao.jdbc;


import com.solovey.movieland.dao.ReportingDao;
import com.solovey.movieland.dao.jdbc.mapper.ReportMovieRowMapper;
import com.solovey.movieland.dao.jdbc.mapper.ReportTopUserRowMapper;
import com.solovey.movieland.entity.reporting.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcReportingDao implements ReportingDao {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private static final ReportMovieRowMapper REPORT_MOVIE_ROW_MAPPER = new ReportMovieRowMapper();
    private static final ReportTopUserRowMapper REPORT_TOP_USER_ROW_MAPPER = new ReportTopUserRowMapper();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private QueryGenerator queryGenerator;

    @Autowired
    private String getMoviesForReportSql;

    @Autowired
    private String saveReportMetadataSql;

    @Autowired
    private String getReportsMetadataSql;

    @Autowired
    private String getMaxReportIdSql;

    @Autowired
    private String removeReportMetadataSql;

    @Autowired
    private String getTopUsersSql;

    @Override
    public List<ReportMovie> getMoviesForReport(Report report) {

        log.debug("Start getting movies for report from DB");
        long startTime = System.currentTimeMillis();

        Date inDateFrom;
        Date inDateTo;
        List<ReportMovie> movieList;
        if (report.getReportType() == ReportType.ADDED_DURING_PERIOD) {
            inDateFrom = Date.valueOf(report.getDateFrom());
            inDateTo = Date.valueOf(report.getDateTo());
            movieList = jdbcTemplate.query(queryGenerator.buildMoviesReportQuery(getMoviesForReportSql, report.getReportType()),
                    REPORT_MOVIE_ROW_MAPPER, inDateFrom, inDateTo);
        } else {
            movieList = jdbcTemplate.query(queryGenerator.buildMoviesReportQuery(getMoviesForReportSql, report.getReportType()),
                    REPORT_MOVIE_ROW_MAPPER);
        }


        log.debug("Finish getting movies for report from DB. It took {} ms", System.currentTimeMillis() - startTime);
        return movieList;
    }

    @Override
    public void saveReportMetadata(Report report) {

        log.debug("Start saving report metadata to DB");
        long startTime = System.currentTimeMillis();
        Date dateFrom;
        Date dateTo;
        if (report.getReportType() == ReportType.ADDED_DURING_PERIOD) {
            dateFrom=Date.valueOf(report.getDateFrom());
            dateTo=Date.valueOf(report.getDateTo());
        } else{
            dateFrom=null;
            dateTo=null;
        }


        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("reportId", report.getReportId())
                .addValue("userId", report.getUserId())
                .addValue("path", report.getPath())
                .addValue("reportType", report.getReportType().getTypeName())
                .addValue("reportOutputType", report.getReportOutputType().getOutputTypeName())
                .addValue("dateFrom", dateFrom)
                .addValue("dateTo", dateTo)
                .addValue("reportState", report.getReportState().getStateName());

        namedParameterJdbcTemplate.update(saveReportMetadataSql, parameters);

        log.debug("Finish saving report metadata to DB. It took {} ms", System.currentTimeMillis() - startTime);

    }

    @Override
    public Map<String, Report> getReportsMetadata() {
        log.debug("Start getting reports metadata from DB");
        long startTime = System.currentTimeMillis();

        Map<String, Report> reports=jdbcTemplate.query(getReportsMetadataSql,new MovieResultSetExtractor());

        log.debug("Finish getting reports metadata from DB. It took {} ms", System.currentTimeMillis() - startTime);
        return reports;
    }


    @Override
    public void removeReportMetadata(String reportId) {
        jdbcTemplate.update(removeReportMetadataSql, reportId);
    }

    @Override
    public List<ReportTopUser> getTopUsers() {
        log.debug("Start getting top users from DB");
        long startTime = System.currentTimeMillis();

        List<ReportTopUser> users = jdbcTemplate.query(getTopUsersSql, REPORT_TOP_USER_ROW_MAPPER);

        log.debug("Finish getting getting top users from DB. It took {} ms", System.currentTimeMillis() - startTime);
        return users;
    }

    private final static class MovieResultSetExtractor implements ResultSetExtractor<Map<String,Report>>{

        @Override
        public Map<String, Report> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            Map<String, Report> reports = new HashMap<>();

            while(resultSet.next()) {
                Report report = new Report();
                report.setReportState(ReportState.getReportState(resultSet.getString("report_state")));
                report.setReportType(ReportType.getTypeName(resultSet.getString("report_type")));
                if (report.getReportType() == ReportType.ADDED_DURING_PERIOD) {
                    report.setDateFrom(resultSet.getDate("date_from").toLocalDate());
                    report.setDateTo(resultSet.getDate("date_to").toLocalDate());
                }
                report.setPath(resultSet.getString("path"));
                report.setReportOutputType(ReportOutputType.getReportOutputType(resultSet.getString("report_output_type")));
                report.setUserId(resultSet.getInt("user_id"));
                report.setReportId(resultSet.getString("report_id"));
                reports.put(resultSet.getString("report_id"), report);
            }
            return reports;
        }
    }
}
