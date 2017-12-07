package com.solovey.movieland.entity.reporting;


public enum ReportOutputType {
    PDF("pdf"),
    XLSX("xlsx");
    private final String reportOutputType;

    ReportOutputType(String reportOutputType) {
        this.reportOutputType = reportOutputType;
    }

    public static ReportOutputType getReportOutputType(String reportType) {

        for (ReportOutputType reportOutputType1 : ReportOutputType.values()) {
            if (reportOutputType1.reportOutputType.equalsIgnoreCase(reportType)) {
                return reportOutputType1;
            }
        }
        throw new IllegalArgumentException("Wrong report output type");
        // UAH in case when wrong parameter value
        //return UserRole.UAH;
    }

    public String getReportOutputType() {
        return reportOutputType;
    }
}
