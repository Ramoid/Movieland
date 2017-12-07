package com.solovey.movieland.dao;


import com.solovey.movieland.entity.reporting.Report;
import com.solovey.movieland.entity.reporting.ReportMovie;
import com.solovey.movieland.entity.reporting.ReportTopUser;

import java.util.List;
import java.util.Map;

public interface ReportingDao {
    List<ReportMovie> getMoviesForReport(Report reportParams);

    void saveReportMetadata(Report report);

    Map<Integer, Report> getReportsMetadata();

    int getMaxReportId();

    void removeReportMetadata(int reportId);

    List<ReportTopUser> getTopUsers();
}
