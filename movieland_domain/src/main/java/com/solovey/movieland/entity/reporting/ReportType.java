package com.solovey.movieland.entity.reporting;


public enum ReportType {
    ALL_MOVIES("allMovies"),
    ADDED_DURING_PERIOD("addedDuringPeriod"),
    TOP_ACTIVE_USERS("topActiveUsers");
    private final String reportType;

    ReportType(String reportType) {
        this.reportType = reportType;
    }

    public static ReportType getReportType(String reportType) {

        for (ReportType reportType1 : ReportType.values()) {
            if (reportType1.reportType.equalsIgnoreCase(reportType)) {
                return reportType1;
            }
        }
        throw new IllegalArgumentException("Wrong report type");
        // UAH in case when wrong parameter value
        //return UserRole.UAH;
    }

    public String getReportType() {
        return reportType;
    }
}
