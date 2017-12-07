package com.solovey.movieland.entity.reporting;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Date;

public class Report {
    private Date dateFrom;
    private Date dateTo;
    private ReportOutputType reportOutputType;
    private ReportType reportType;
    private String link;
    @JsonIgnore
    private int userId;
    private ReportState reportState;
    private int reportId;
    @JsonIgnore
    private String userEmail;

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
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
                ", link='" + link + '\'' +
                ", userId=" + userId +
                ", reportState=" + reportState +
                ", reportId=" + reportId +
                '}';
    }
}
