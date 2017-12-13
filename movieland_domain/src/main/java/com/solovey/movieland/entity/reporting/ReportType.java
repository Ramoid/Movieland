package com.solovey.movieland.entity.reporting;


public enum ReportType {
    ALL_MOVIES("allMovies"),
    ADDED_DURING_PERIOD("addedDuringPeriod"),
    TOP_ACTIVE_USERS("topActiveUsers");
    private final String typeName;

    ReportType(String reportType) {
        this.typeName = reportType;
    }

    public static ReportType getTypeName(String inReportType) {

        for (ReportType reportType : ReportType.values()) {
            if (reportType.typeName.equalsIgnoreCase(inReportType)) {
                return reportType;
            }
        }
        throw new IllegalArgumentException("Wrong report type " + inReportType);

    }

    public String getTypeName() {
        return typeName;
    }
}
