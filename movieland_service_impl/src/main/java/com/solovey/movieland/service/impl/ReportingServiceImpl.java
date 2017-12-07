package com.solovey.movieland.service.impl;

import com.solovey.movieland.dao.ReportingDao;
import com.solovey.movieland.entity.reporting.*;
import com.solovey.movieland.service.ReportingService;
import com.solovey.movieland.service.impl.generator.EmailSender;
import com.solovey.movieland.service.impl.generator.ReportGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class ReportingServiceImpl implements ReportingService {

    private Queue<Report> reportRequests = new ConcurrentLinkedQueue<>();
    private Map<Integer, Report> reportsMetadataCache = new ConcurrentHashMap<>();
    private AtomicInteger lastReportId = new AtomicInteger();
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    @Autowired
    private ExecutorService threadPool;

    @Autowired
    private ReportingDao reportingDao;

    @Autowired
    private ReportGenerator reportGenerator;

    @Autowired
    private EmailSender emailSender;

    @Override
    public void addReportRequest(Report report) {
        report.setReportId(lastReportId.incrementAndGet());
        reportRequests.add(report);
    }

    @Override
    public ReportState getReportStatus(int reportId, int userId) {
        lock.readLock().lock();
        try {
            Report report = reportsMetadataCache.get(reportId);
            if (report != null && report.getUserId() == userId) {
                if (report.getUserId() == userId) {
                    return report.getReportState();
                } else {
                    throw new RuntimeException("You do not have access to this report");
                }
            }
            for (Report report1 : reportRequests) {
                if (report1.getReportId() == reportId) {
                    if (report1.getUserId() == userId) {
                        return ReportState.NEW;
                    } else {
                        throw new RuntimeException("You do not have access to this report");
                    }
                }
            }
            throw new RuntimeException("Report id does not exist");
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
    public Report getReportMetadata(int reportId, int userId) {
        Report report = reportsMetadataCache.get(reportId);
        if (report != null && report.getReportState() == ReportState.READY && report.getUserId() == userId) {
            return report;
        }
        throw new RuntimeException("Report does not exist or in progress");
    }

    @Override
    public void deleteReport(int reportId, int userId) {
        lock.writeLock().lock();
        try {
            Iterator<Report> iterator = reportRequests.iterator();
            while (iterator.hasNext()) {
                Report report = iterator.next();
                if (report.getReportId() == reportId) {
                    if (report.getUserId() == userId) {
                        iterator.remove();
                        return;
                    } else {
                        throw new RuntimeException("You cannot remove this report");
                    }
                }
            }
        } finally {
            lock.writeLock().unlock();
        }

        Report report = reportsMetadataCache.get(reportId);
        if (report != null) {
            if (report.getUserId() != userId) {
                throw new RuntimeException("You cannot remove this report");
            }
            if (report.getReportState() != ReportState.IN_PROGRESS) {
                reportsMetadataCache.remove(reportId);
                deleteReportFile(report);
                reportingDao.removeReportMetadata(reportId);
            } else {
                Thread deleteTask = new Thread(() -> {
                    while (true) {
                        if (report.getReportState() != ReportState.IN_PROGRESS) {
                            deleteReportFile(report);
                            reportingDao.removeReportMetadata(reportId);
                            return;
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                });
                deleteTask.start();
            }
        }


    }

    private void deleteReportFile(Report report) {
        String fileName = "reports/Report" + report.getReportId() + "." +
                report.getReportOutputType().getReportOutputType();
        File file = new File(fileName);
        file.delete();
    }

    @Scheduled(fixedDelayString = "${reporting.sheduler.interval}", initialDelayString = "${reporting.sheduler.interval}")
    private void processRequests() {
        lock.writeLock().lock();
        try {
            while (true) {
                Report report = reportRequests.poll();
                if (report != null) {
                    threadPool.submit(() -> reportGenerationTask(report));
                } else {
                    break;
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void reportGenerationTask(Report report) {

        report.setReportState(ReportState.IN_PROGRESS);
        reportsMetadataCache.put(report.getReportId(), report);
        String reportFileName;

        if (report.getReportType() == ReportType.TOP_ACTIVE_USERS) {
            List<ReportTopUser> topUsers = reportingDao.getTopUsers();

            if (report.getReportOutputType() == ReportOutputType.XLSX) {
                reportFileName = reportGenerator.generateTopUsersXlsxReport(topUsers, report);
            } else {
                reportFileName = reportGenerator.generateTopUsersPdfReport(topUsers, report);
            }
        } else {
            List<ReportMovie> movieList = reportingDao.getMoviesForReport(report);

            if (report.getReportOutputType() == ReportOutputType.XLSX) {
                reportFileName = reportGenerator.generateMoviesXlsxReport(movieList, report);
            } else {
                reportFileName = "";
            }
        }

        report.setLink(report.getLink().replace("?", reportFileName));
        reportingDao.saveReportMetadata(report);

        if (report.getReportState() != ReportState.ERROR) {
            report.setReportState(ReportState.READY);
        }
        emailSender.sendEmail(report);

    }


    @PostConstruct
    private void init() {
        reportsMetadataCache.putAll(reportingDao.getReportsMetadata());
        lastReportId.set(reportingDao.getMaxReportId());
    }
}
