package com.solovey.movieland.entity.reporting;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;

public class Report {
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private ReportOutputType reportOutputType;
    private ReportType reportType;
    private String path;
    private int userId;
    private ReportState reportState;
    private String reportId;
    private String userEmail;
    private CountDownLatch countDownLatch;

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public ReportState getReportState() {
        return reportState;
    }

    public void setReportState(ReportState reportState) {
        this.reportState = reportState;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Override
    public String toString() {
        return "Report{" +
                "dateFrom=" + dateFrom +
                ", dateTo=" + dateTo +
                ", reportOutputType=" + reportOutputType +
                ", reportType=" + reportType +
                ", path='" + path + '\'' +
                ", userId=" + userId +
                ", reportState=" + reportState +
                ", reportId=" + reportId +
                '}';
    }
}
