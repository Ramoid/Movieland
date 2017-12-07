package com.solovey.movieland.web.util.dto;


public class ReportStatusDto {
    private String reportStatus;

    public ReportStatusDto(String reportStatus) {
        this.reportStatus = reportStatus;
    }

    public String getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(String reportStatus) {
        this.reportStatus = reportStatus;
    }
}
