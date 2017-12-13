package com.solovey.movieland.entity.reporting;


public enum ReportOutputType {
    PDF("pdf"),
    XLSX("xlsx");
    private final String outputTypeName;

    ReportOutputType(String outputTypeName) {
        this.outputTypeName = outputTypeName;
    }

    public static ReportOutputType getReportOutputType(String inReportOutputType) {

        for (ReportOutputType reportOutputType : ReportOutputType.values()) {
            if (reportOutputType.outputTypeName.equalsIgnoreCase(inReportOutputType)) {
                return reportOutputType;
            }
        }
        throw new IllegalArgumentException("Wrong report output type " + inReportOutputType);
    }

    public String getOutputTypeName() {
        return outputTypeName;
    }
}
