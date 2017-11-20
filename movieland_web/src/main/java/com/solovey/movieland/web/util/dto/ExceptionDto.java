package com.solovey.movieland.web.util.dto;

public class ExceptionDto {
    private String exceptionText;

    public ExceptionDto(String exceptionText) {
        this.exceptionText = exceptionText;
    }

    public String getExceptionText() {
        return exceptionText;
    }

    public void setExceptionText(String exceptionText) {
        this.exceptionText = exceptionText;
    }

    @Override
    public String toString() {
        return "ExceptionDto{" +
                "exceptionText='" + exceptionText + '\'' +
                '}';
    }
}
