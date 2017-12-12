package com.solovey.movieland.entity.reporting;


public enum ReportState {
    READY("Ready"),
    IN_PROGRESS("InProgress"),
    NEW("New"),
    ERROR("Error");
    private final String stateName;

    ReportState(String stateName) {
        this.stateName = stateName;
    }

    public static ReportState getReportState(String inReportState) {

        for (ReportState reportState : ReportState.values()) {
            if (reportState.stateName.equalsIgnoreCase(inReportState)) {
                return reportState;
            }
        }
        throw new IllegalArgumentException("Wrong report state " + inReportState);

    }

    public String getStateName() {
        return stateName;
    }
}
