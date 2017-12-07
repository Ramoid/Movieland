package com.solovey.movieland.web.controller;

import com.solovey.movieland.dao.enums.UserRole;
import com.solovey.movieland.entity.reporting.Report;
import com.solovey.movieland.service.ReportingService;
import com.solovey.movieland.web.security.Protected;
import com.solovey.movieland.web.security.entity.PrincipalUser;
import com.solovey.movieland.web.util.dto.ReportStatusDto;
import com.solovey.movieland.web.util.json.JsonJacksonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
@RequestMapping(value = "/report")
public class ReportingController {

    private final ReportingService reportingService;
    private final Logger log = LoggerFactory.getLogger(getClass());
    private JsonJacksonConverter jsonConverter;

    @Autowired
    public ReportingController(ReportingService reportingService, JsonJacksonConverter jsonJacksonConverter) {
        this.reportingService = reportingService;
        this.jsonConverter = jsonJacksonConverter;
    }

    @RequestMapping(method = POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Protected(roles = {UserRole.ADMIN})
    public void addRequest(@RequestBody String requestJson, PrincipalUser principal, HttpServletRequest request) {
        log.info("Sending request to add report request");
        long startTime = System.currentTimeMillis();

        reportingService.addReportRequest(jsonConverter.parseJsonToReport(requestJson, principal, request.getRequestURL().append("/download/?").toString()));

        log.info("Request was added. It took {} ms", System.currentTimeMillis() - startTime);

    }

    @RequestMapping(value = "/{reportId}", method = PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Protected(roles = {UserRole.ADMIN})
    public ReportStatusDto getReportStatus(@PathVariable int reportId, PrincipalUser principal) {
        log.info("Sending request to get report {} status", reportId);
        long startTime = System.currentTimeMillis();

        ReportStatusDto reportStatusDto = new ReportStatusDto(reportingService.getReportStatus(reportId, principal.getUserId()).getReportState());

        log.info("Report status was received. It took {} ms", System.currentTimeMillis() - startTime);
        return reportStatusDto;

    }

    @RequestMapping(method = PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Protected(roles = {UserRole.ADMIN})
    public List<Report> getUserReports(PrincipalUser principal) {
        log.info("Sending request to get user {} reports", principal.getUserId());
        long startTime = System.currentTimeMillis();

        List<Report> reportList = reportingService.getUserReports(principal.getUserId());

        log.info("User reports were received. It took {} ms", System.currentTimeMillis() - startTime);
        return reportList;

    }

    @RequestMapping(value = "/{reportId}", method = GET, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    @Protected(roles = {UserRole.ADMIN})
    public String getReportLink(@PathVariable int reportId, PrincipalUser principal) {
        return reportingService.getReportMetadata(reportId, principal.getUserId()).getLink();

    }

    @RequestMapping(value = "/{reportId}", method = DELETE)
    @ResponseBody
    @Protected(roles = {UserRole.ADMIN})
    public void deleteReport(@PathVariable int reportId, PrincipalUser principal) {
        reportingService.deleteReport(reportId, principal.getUserId());
    }

    @RequestMapping(value = "/download/{reportFileName:.+}", method = GET)
    @ResponseBody
    //@Protected(roles = {UserRole.ADMIN})
    public void downloadReport(HttpServletResponse response, @PathVariable String reportFileName) {
        Path file = Paths.get("reports/" + reportFileName);
        if (Files.exists(file)) {
            //response.setContentType("application/pdf");
            String mimeType = URLConnection.guessContentTypeFromName(file.getFileName().toString());
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }

            log.info("mimetype : " + mimeType);

            response.setContentType(mimeType);
            response.addHeader("Content-Disposition", "attachment; filename=" + reportFileName);
            try {
                Files.copy(file, response.getOutputStream());
                response.getOutputStream().flush();
            } catch (IOException e) {
                log.error("error downloading file {} {}", reportFileName, e);
                throw new RuntimeException("error downloading file ", e);
            }
        } else {
            throw new RuntimeException("file does not exist");
        }

    }


}
