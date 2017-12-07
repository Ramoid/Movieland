package com.solovey.movieland.dao.jdbc;


import com.solovey.movieland.dao.ReportingDao;
import com.solovey.movieland.dao.jdbc.mapper.ReportMovieRowMapper;
import com.solovey.movieland.dao.jdbc.mapper.ReportTopUserRowMapper;
import com.solovey.movieland.entity.reporting.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.sql.Date;
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
    public List<ReportMovie> getMoviesForReport(Report reportParams) {

        log.debug("Start getting movies for report from DB");
        long startTime = System.currentTimeMillis();

        Date inDateFrom;
        Date inDateTo;
        if (reportParams.getReportType() == ReportType.ALL_MOVIES) {
            inDateFrom = Date.valueOf("1900-01-01");
            inDateTo = Date.valueOf("3000-01-01");
        } else {
            inDateFrom = reportParams.getDateFrom();
            inDateTo = reportParams.getDateTo();
        }
        List<ReportMovie> movieList = jdbcTemplate.query(getMoviesForReportSql, REPORT_MOVIE_ROW_MAPPER, inDateFrom, inDateTo);
        log.debug("Finish getting movies for report from DB. It took {} ms", System.currentTimeMillis() - startTime);
        return movieList;
    }

    @Override
    public void saveReportMetadata(Report report) {

        log.debug("Start saving report metadata to DB");
        long startTime = System.currentTimeMillis();

        jdbcTemplate.update(saveReportMetadataSql, report.getReportId(), report.getUserId(), report.getLink(),
                report.getReportType().getReportType(), report.getReportOutputType().getReportOutputType(), report.getDateFrom(), report.getDateTo());

        log.debug("Finish saving report metadata to DB. It took {} ms", System.currentTimeMillis() - startTime);

    }

    @Override
    public Map<Integer, Report> getReportsMetadata() {
        log.debug("Start getting reports metadata from DB");
        long startTime = System.currentTimeMillis();
        Map<Integer, Report> reports = new HashMap<>();

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(getReportsMetadataSql);
        while (sqlRowSet.next()) {
            Report report = new Report();
            report.setReportState(ReportState.READY);
            report.setDateFrom(sqlRowSet.getDate("date_from"));
            report.setDateTo(sqlRowSet.getDate("date_to"));
            report.setLink(sqlRowSet.getString("link"));
            report.setReportOutputType(ReportOutputType.getReportOutputType(sqlRowSet.getString("report_output_type")));
            report.setReportType(ReportType.getReportType(sqlRowSet.getString("report_type")));
            report.setUserId(sqlRowSet.getInt("user_id"));
            report.setReportId(sqlRowSet.getInt("report_id"));

            reports.put(sqlRowSet.getInt("report_id"), report);
        }

        log.debug("Finish getting reports metadata from DB. It took {} ms", System.currentTimeMillis() - startTime);
        return reports;
    }

    @Override
    public int getMaxReportId() {
        return jdbcTemplate.queryForObject(getMaxReportIdSql, Integer.class);
    }

    @Override
    public void removeReportMetadata(int reportId) {
        jdbcTemplate.update(removeReportMetadataSql, reportId);
    }

    @Override
    public List<ReportTopUser> getTopUsers() {
        log.debug("Start getting top users from DB");
        long startTime = System.currentTimeMillis();

        List<ReportTopUser> users = jdbcTemplate.query(getTopUsersSql,REPORT_TOP_USER_ROW_MAPPER);

        log.debug("Finish getting getting top users from DB. It took {} ms", System.currentTimeMillis() - startTime);
        return  users;
    }
}
