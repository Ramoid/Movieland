package com.solovey.movieland.service.impl;

import com.solovey.movieland.dao.ReportingDao;
import com.solovey.movieland.entity.reporting.*;
import com.solovey.movieland.service.ReportingService;
import com.solovey.movieland.service.impl.reporting.EmailSender;
import com.solovey.movieland.service.impl.reporting.ReportGenerator;
import com.solovey.movieland.service.impl.reporting.ReportNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class ReportingServiceImpl implements ReportingService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private Queue<Report> reportRequests = new ConcurrentLinkedQueue<>();
    private Map<String, Report> reportsMetadataCache = new ConcurrentHashMap<>();

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    @Autowired
    @Qualifier("fixedThreadPoolExecutor")
    private ExecutorService singleThreadPoolExecutor;

    @Autowired
    private ReportingDao reportingDao;

    @Autowired
    private ReportGenerator reportGenerator;

    @Autowired
    private EmailSender emailSender;

    @Override
    public void addReportRequest(Report report) {
        report.setReportId(UUID.randomUUID().toString());
        reportRequests.add(report);
    }

    @Override
    public ReportState getReportStatus(String reportId, int userId) {
        lock.readLock().lock();
        try {
            Report report = reportsMetadataCache.get(reportId);
            if (report != null) {
                if (report.getUserId() == userId) {
                    return report.getReportState();
                } else {
                    throw new SecurityException("You do not have access to this report");
                }
            }
            for (Report report1 : reportRequests) {
                if (report1.getReportId().equals(reportId)) {
                    if (report1.getUserId() == userId) {
                        return ReportState.NEW;
                    } else {
                        throw new SecurityException("You do not have access to this report");
                    }
                }
            }
            throw new ReportNotFoundException();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<Report> getUserReports(int userId) {
        List<Report> userReports = new ArrayList<>();

        lock.readLock().lock();
        try {
            for (Report report : reportRequests) {
                if (report.getUserId() == userId) {
                    userReports.add(report);
                }
            }

            for (Report report : reportsMetadataCache.values()) {
                if (report.getUserId() == userId) {
                    userReports.add(report);
                }
            }

        } finally {
            lock.readLock().unlock();
        }
        return userReports;
    }

    @Override
    public Report getReportMetadata(String reportId, int userId) {
        Report report = reportsMetadataCache.get(reportId);
        if (report != null && report.getReportState() == ReportState.READY && report.getUserId() == userId) {
            return report;
        }
        throw new ReportNotFoundException();
    }

    @Override
    public void deleteReport(String reportId, int userId) {
        lock.writeLock().lock();
        try {
            Iterator<Report> iterator = reportRequests.iterator();
            while (iterator.hasNext()) {
                Report report = iterator.next();
                if (report.getReportId().equals(reportId)) {
                    if (report.getUserId() == userId) {
                        iterator.remove();
                        log.info("Report {} has been removed from queue", report.getReportId());
                        return;
                    } else {
                        throw new SecurityException("You cannot remove this report");
                    }
                }
            }
        } finally {
            lock.writeLock().unlock();
        }

        Report report = reportsMetadataCache.get(reportId);
        if (report != null) {
            if (report.getUserId() != userId) {
                throw new SecurityException("You cannot remove this report");
            }
            reportsMetadataCache.remove(reportId);

            if (report.getReportState() != ReportState.IN_PROGRESS) {
                deleteReportFile(report);
                reportingDao.removeReportMetadata(reportId);
                log.info("Report {} has been removed", report.getReportId());
            } else {
                singleThreadPoolExecutor.submit(() -> {
                    try {
                        report.getCountDownLatch().await();
                        deleteReportFile(report);
                    } catch (InterruptedException e) {
                        log.warn("thread that deletes report is interrupted");
                    }

                    reportingDao.removeReportMetadata(reportId);
                    log.info("Report {} has been removed", report.getReportId());
                });

            }
        }


    }

    private void deleteReportFile(Report report) {
        String fileName = report.getPath();
        File file = new File(fileName);
        file.delete();
    }

    @Scheduled(fixedDelayString = "${reporting.sheduler.interval}",
            initialDelayString = "${reporting.sheduler.interval}")
    private void processRequests() {
        lock.writeLock().lock();
        int i = 0;
        try {
            while (i == 0) {
                Report report = reportRequests.poll();
                if (report == null) {
                    i++;
                } else {
                    singleThreadPoolExecutor.submit(() -> reportGenerationTask(report));
                }
            }

        } finally {
            lock.writeLock().unlock();
        }
    }

    private void reportGenerationTask(Report report) {
        report.setCountDownLatch(new CountDownLatch(1));
        report.setReportState(ReportState.IN_PROGRESS);

        reportsMetadataCache.put(report.getReportId(), report);
        String reportFilePath;

        if (report.getReportType() == ReportType.TOP_ACTIVE_USERS) {
            List<ReportTopUser> topUsers = reportingDao.getTopUsers();

            if (report.getReportOutputType() == ReportOutputType.XLSX) {
                reportFilePath = reportGenerator.generateTopUsersXlsxReport(topUsers, report);
            } else {
                reportFilePath = reportGenerator.generateTopUsersPdfReport(topUsers, report);
            }
        } else {
            List<ReportMovie> movieList = reportingDao.getMoviesForReport(report);

            if (report.getReportOutputType() == ReportOutputType.XLSX) {
                reportFilePath = reportGenerator.generateMoviesXlsxReport(movieList, report);
            } else {
                reportFilePath = reportGenerator.generateMoviesPdfReport(movieList, report);
            }
        }

        report.setPath(reportFilePath);

        if (report.getReportState() != ReportState.ERROR) {
            report.setReportState(ReportState.READY);
        }
        reportingDao.saveReportMetadata(report);
        report.getCountDownLatch().countDown();
        emailSender.sendEmail(report);

    }


    @PostConstruct
    private void init() {
        reportsMetadataCache.putAll(reportingDao.getReportsMetadata());

    }
}
