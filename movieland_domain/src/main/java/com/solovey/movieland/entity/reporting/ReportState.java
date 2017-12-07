package com.solovey.movieland.entity.reporting;


public enum ReportState {
    READY("Ready"),
    IN_PROGRESS("InProgress"),
    NEW("New"),
    ERROR("Error");
    private final String reportState;

    ReportState(String reportState) {
        this.reportState = reportState;
    }

    public static ReportState getReportState(String reportType) {

        for (ReportState reportState1 : ReportState.values()) {
            if (reportState1.reportState.equalsIgnoreCase(reportType)) {
                return reportState1;
            }
        }
        throw new IllegalArgumentException("Wrong report state");
        // UAH in case when wrong parameter value
        //return UserRole.UAH;
    }

    public String getReportState() {
        return reportState;
    }
}
