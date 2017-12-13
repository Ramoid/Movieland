package com.solovey.movieland.web.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ReportFileDao {
    private final Logger log = LoggerFactory.getLogger(getClass());

    public void uploadFile(HttpServletResponse response, String reportFileName){
        Path file = Paths.get("reports/" + reportFileName);
        if (Files.exists(file)) {

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
