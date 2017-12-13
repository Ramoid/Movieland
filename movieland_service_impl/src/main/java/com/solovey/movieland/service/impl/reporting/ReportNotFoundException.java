package com.solovey.movieland.service.impl.reporting;


public class ReportNotFoundException extends RuntimeException {
    public ReportNotFoundException() {
        super("Report does not exist/ready");
    }
}
