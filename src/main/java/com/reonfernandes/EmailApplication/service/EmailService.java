package com.reonfernandes.EmailApplication.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface EmailService {
    void sendEmail (String receiver, String subject, String message);   // email to single person
    void sendEmail (String[] receivers, String subject, String message);    // email to multiple people
    void sendEmailWithHTML (String receiver, String subject, String htmlContent);   // single person --> html content
    void sendEmailWithHTML (String[] receivers, String subject, String htmlContent);    // multiple people --> html content
    void sendEmailWithFile(String receiver, String subject, String message, File file);     // single person --> file
    void sendEmailWithFile(String[] receivers, String subject, String message, File file);  // multiple people --> file
    void sendEmailWithFileStream(String receiver, String subject, String message, MultipartFile file);
}
