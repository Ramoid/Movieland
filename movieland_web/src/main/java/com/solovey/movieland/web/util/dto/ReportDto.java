package com.solovey.movieland.web.util.dto;


import com.solovey.movieland.entity.reporting.ReportOutputType;
import com.solovey.movieland.entity.reporting.ReportState;
import com.solovey.movieland.entity.reporting.ReportType;

import java.time.LocalDate;

public class ReportDto {
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private ReportOutputType reportOutputType;
    private ReportType reportType;
    private String path;
    private ReportState reportState;
    private String reportId;

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }

    public ReportOutputType getReportOutputType() {
        return reportOutputType;
    }

    public void setReportOutputType(ReportOutputType reportOutputType) {
        this.reportOutputType = reportOutputType;
    }

    public ReportType getReportType() {
        return reportType;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }



    public ReportState getReportState() {
        return reportState;
    }

    public void setReportState(ReportState reportState) {
        this.reportState = reportState;
    }



    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }


    @Override
    public String toString() {
        return "Report{" +
                "dateFrom=" + dateFrom +
                ", dateTo=" + dateTo +
                ", reportOutputType=" + reportOutputType +
                ", reportType=" + reportType +
                ", path='" + path + '\'' +
                ", reportState=" + reportState +
                ", reportId=" + reportId +
                '}';
    }
}
