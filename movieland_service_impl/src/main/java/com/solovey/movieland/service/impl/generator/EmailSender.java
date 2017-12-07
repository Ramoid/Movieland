package com.solovey.movieland.service.impl.generator;


import com.solovey.movieland.entity.reporting.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class EmailSender {

   @Autowired
   @Qualifier("emailAppenderProperties")
   private Properties emailSenderProperties;


    public  void sendEmail(Report report){

        Session session = Session.getDefaultInstance(emailSenderProperties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(emailSenderProperties.getProperty("username"),emailSenderProperties.getProperty("password"));
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailSenderProperties.getProperty("username")));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(report.getUserEmail()));
            message.setSubject("Report ready");
            message.setText("Dear Admin, your report is ready " + report);

            Transport.send(message);


        } catch (MessagingException e) {
            throw new RuntimeException("EmailSender failed with sending", e);
        }
    }
}
