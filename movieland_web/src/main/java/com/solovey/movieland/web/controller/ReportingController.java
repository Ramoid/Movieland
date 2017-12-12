package com.solovey.movieland.web.controller;

import com.solovey.movieland.dao.enums.UserRole;

import com.solovey.movieland.service.ReportingService;
import com.solovey.movieland.web.security.Protected;
import com.solovey.movieland.web.security.entity.PrincipalUser;
import com.solovey.movieland.web.util.ReportFileDao;
import com.solovey.movieland.web.util.dto.ReportDto;
import com.solovey.movieland.web.util.dto.ReportStatusDto;
import com.solovey.movieland.web.util.dto.ToDtoConverter;
import com.solovey.movieland.web.util.json.JsonJacksonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;


import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
@RequestMapping(value = "/report")
public class ReportingController {

    private ReportingService reportingService;
    private final Logger log = LoggerFactory.getLogger(getClass());
    private JsonJacksonConverter jsonConverter;
    private ToDtoConverter toDtoConverter;
    private ReportFileDao reportFileDao;

    @Autowired
    public ReportingController(ReportingService reportingService, JsonJacksonConverter jsonJacksonConverter,
                               ToDtoConverter toDtoConverter, ReportFileDao reportFileDao) {
        this.reportingService = reportingService;
        this.jsonConverter = jsonJacksonConverter;
        this.toDtoConverter = toDtoConverter;
        this.reportFileDao = reportFileDao;
    }

    @RequestMapping(method = POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Protected(roles = {UserRole.ADMIN})
    public void addRequest(@RequestBody String requestJson, PrincipalUser principal) {
        log.info("Sending request to add report request");
        long startTime = System.currentTimeMillis();

        reportingService.addReportRequest(jsonConverter.parseJsonToReport(requestJson, principal));

        log.info("Request was added. It took {} ms", System.currentTimeMillis() - startTime);

    }

    @RequestMapping(value = "/{reportId}", method = PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Protected(roles = {UserRole.ADMIN})
    public ReportStatusDto getReportStatus(@PathVariable String reportId, PrincipalUser principal) {
        log.info("Sending request to get report {} status", reportId);
        long startTime = System.currentTimeMillis();

        ReportStatusDto reportStatusDto = new ReportStatusDto(reportingService.getReportStatus(reportId, principal.getUserId()).getStateName());

        log.info("Report status was received. It took {} ms", System.currentTimeMillis() - startTime);
        return reportStatusDto;

    }

    @RequestMapping(method = PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Protected(roles = {UserRole.ADMIN})
    public List<ReportDto> getUserReports(PrincipalUser principal, HttpServletRequest request) {
        log.info("Sending request to get user {} reports", principal.getUserId());
        long startTime = System.currentTimeMillis();

        StringBuffer reportUrl = request.getRequestURL();
        List<ReportDto> reportDtoList = toDtoConverter.convertReportsToReportsDto(
                reportingService.getUserReports(principal.getUserId()));

        for (ReportDto report : reportDtoList) {
            if (report.getPath() != null) {
                report.setPath(reportUrl.append("/download/").append(new File(report.getPath()).getName()).toString());
            }
        }

        log.info("User reports were received. It took {} ms", System.currentTimeMillis() - startTime);
        return reportDtoList;

    }

    @RequestMapping(value = "/{reportId}", method = GET, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    @Protected(roles = {UserRole.ADMIN})
    public String getReportLink(@PathVariable String reportId, PrincipalUser principal, HttpServletRequest request) {
        System.out.println(request.getRequestURL());
        System.out.println(request.getContextPath());
        System.out.println(request.getPathTranslated());
        System.out.println(request.getPathInfo());
        return reportingService.getReportMetadata(reportId, principal.getUserId()).getPath();

    }

    @RequestMapping(value = "/{reportId}", method = DELETE)
    @ResponseBody
    @Protected(roles = {UserRole.ADMIN})
    public void deleteReport(@PathVariable String reportId, PrincipalUser principal) {
        reportingService.deleteReport(reportId, principal.getUserId());
    }

    @RequestMapping(value = "/download/{reportFileName:.+}", method = GET)
    @ResponseBody
    //@Protected(roles = {UserRole.ADMIN})
    public void downloadReport(HttpServletResponse response, @PathVariable String reportFileName) {
        reportFileDao.uploadFile(response, reportFileName);

    }

}
