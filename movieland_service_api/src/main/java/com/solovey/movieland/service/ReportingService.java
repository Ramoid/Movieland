package com.solovey.movieland.service;

import com.solovey.movieland.entity.reporting.Report;
import com.solovey.movieland.entity.reporting.ReportState;

import java.util.List;


public interface ReportingService {

    void addReportRequest(Report reportParams);

    ReportState getReportStatus(int reportId, int userId);

    List<Report> getUserReports(int userId);

    Report getReportMetadata(int reportId, int userId);

    void deleteReport(int reportId, int userId);

}
