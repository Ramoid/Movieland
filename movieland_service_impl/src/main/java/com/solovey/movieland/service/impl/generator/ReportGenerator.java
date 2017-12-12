package com.solovey.movieland.service.impl.generator;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.solovey.movieland.entity.reporting.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ReportGenerator {

    private final Logger log = LoggerFactory.getLogger(getClass());

    public String generateMoviesXlsxReport(List<ReportMovie> movieList, Report report) {
        File excelFile = createReportFile(report);

        int rowNum = 0;
        int cellNum;

        Workbook book = new XSSFWorkbook();
        Sheet sheet = book.createSheet("Movies");


        for (ReportMovie movie : movieList) {

            Row row = sheet.createRow(rowNum++);
            cellNum = 0;

            Cell cell = row.createCell(cellNum++);
            cell.setCellValue(movie.getMovieId());

            cell = row.createCell(cellNum++);
            cell.setCellValue(movie.getTitle());

            cell = row.createCell(cellNum++);
            cell.setCellValue(movie.getDescription());

            cell = row.createCell(cellNum++);
            cell.setCellValue(movie.getGenre());

            cell = row.createCell(cellNum++);
            cell.setCellValue(movie.getPrice());

            cell = row.createCell(cellNum++);
            DataFormat format = book.createDataFormat();
            CellStyle dateStyle = book.createCellStyle();
            dateStyle.setDataFormat(format.getFormat("dd.mm.yyyy"));
            cell.setCellStyle(dateStyle);
            cell.setCellValue(movie.getAddedDate());

            cell = row.createCell(cellNum++);
            cell.setCellStyle(dateStyle);
            cell.setCellValue(movie.getModifiedDate());

            cell = row.createCell(cellNum++);
            cell.setCellValue(movie.getRating());

            cell = row.createCell(cellNum++);
            cell.setCellValue(movie.getReviewsCount());
        }

        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(5);
        sheet.autoSizeColumn(6);

        try (FileOutputStream outputStream = new FileOutputStream(excelFile);) {
            book.write(outputStream);
            return excelFile.getPath();
        } catch (IOException e) {
            report.setReportState(ReportState.ERROR);
            log.error(" error report generating ", e);
            throw new RuntimeException("generateMoviesReport error saving report", e);
        }

    }

    public String generateTopUsersXlsxReport(List<ReportTopUser> topUsers, Report report) {
        File excelFile = createReportFile(report);

        int rowNum = 0;
        int cellNum;

        Workbook book = new XSSFWorkbook();
        Sheet sheet = book.createSheet("TopUsers");


        for (ReportTopUser user : topUsers) {

            Row row = sheet.createRow(rowNum++);
            cellNum = 0;

            Cell cell = row.createCell(cellNum++);
            cell.setCellValue(user.getUserId());

            cell = row.createCell(cellNum++);
            cell.setCellValue(user.getEmail());

            cell = row.createCell(cellNum++);
            cell.setCellValue(user.getReviewsCount());

            cell = row.createCell(cellNum++);
            cell.setCellValue(user.getAverageRate());
        }

        sheet.autoSizeColumn(1);

        try (FileOutputStream outputStream = new FileOutputStream(excelFile);) {
            book.write(outputStream);
            return excelFile.getPath();
        } catch (IOException e) {
            report.setReportState(ReportState.ERROR);
            log.error(" error report generating ", e);
            throw new RuntimeException("generateTopUsersReport error saving report", e);
        }

    }

    public String generateTopUsersPdfReport(List<ReportTopUser> topUsers, Report report) {
        File pdflFile = createReportFile(report);
        Document document = new Document();
        PdfWriter writer=null;

        try {
            writer = PdfWriter.getInstance(document, new FileOutputStream(pdflFile));
            document.open();

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            float[] columnWidths = {1f, 1f, 1f, 1f};
            table.setWidths(columnWidths);

            for (ReportTopUser user : topUsers) {
                table.addCell(String.valueOf(user.getUserId()));
                table.addCell(user.getEmail());
                table.addCell(String.valueOf(user.getReviewsCount()));
                table.addCell(String.valueOf(user.getAverageRate()));
            }

            document.add(table);

            return pdflFile.getPath();

        } catch (FileNotFoundException | DocumentException e) {
            report.setReportState(ReportState.ERROR);
            log.error(" error creating pdf report ", e);
            throw new RuntimeException("error creating pdf report ", e);
        } finally {
            document.close();
            if (writer!=null) {
                writer.close();
            }

        }

    }

    public String generateMoviesPdfReport(List<ReportMovie> movies, Report report) {
        File pdflFile = createReportFile(report);
        Document document = new Document();


        PdfWriter writer=null;
        try {
            BaseFont bf = BaseFont.createFont("OpenSans-Regular.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED); //подключаем файл шрифта, который поддерживает кириллицу
            com.itextpdf.text.Font font = new com.itextpdf.text.Font(bf); //FontFactory.getFont("arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED,0.8f);
            writer = PdfWriter.getInstance(document, new FileOutputStream(pdflFile));
            document.open();

            PdfPTable table = new PdfPTable(9);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            float[] columnWidths = {1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f};
            table.setWidths(columnWidths);

            for (ReportMovie movie : movies) {
                table.addCell(String.valueOf(movie.getMovieId()));
                table.addCell(movie.getTitle());
                table.addCell(new Phrase(movie.getDescription(),font));
                table.addCell(movie.getGenre());
                table.addCell(String.valueOf(movie.getPrice()));
                table.addCell(movie.getAddedDate().toString());
                table.addCell(movie.getModifiedDate().toString());
                table.addCell(String.valueOf(movie.getRating()));
                table.addCell(String.valueOf(movie.getReviewsCount()));
            }

            document.add(table);

            return pdflFile.getPath();

        } catch ( DocumentException | IOException e) {
            report.setReportState(ReportState.ERROR);
            log.error(" error creating pdf report ", e);
            throw new RuntimeException("error creating pdf report ", e);

        } finally {
            document.close();
            if (writer!=null) {
                writer.close();
            }

        }

    }

    private File createReportFile(Report report) {
        try {
            File directory = new File("reports");
            if (!directory.exists()) {
                directory.mkdir();
            }
            File file = new File("reports/" + report.getReportId() + "." + report.getReportOutputType().getOutputTypeName());
            file.createNewFile();
            return file;
        } catch (IOException e) {
            report.setReportState(ReportState.ERROR);
            log.error(" error creating directory/file", e);
            throw new RuntimeException("generateMoviesReport error creating directory/file ", e);
        }
    }
}
