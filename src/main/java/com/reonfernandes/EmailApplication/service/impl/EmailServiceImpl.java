package com.reonfernandes.EmailApplication.service.impl;

import com.reonfernandes.EmailApplication.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String senderEmailId = "reonjervasiofernandes@gmail.com";

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(String receiver, String subject, String message) {
        logger.info("(Service) Sending email to: {}", receiver);

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(receiver);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);
        simpleMailMessage.setFrom(senderEmailId);

        mailSender.send(simpleMailMessage);
        logger.info("Email has been sent..");
    }

    @Override
    public void sendEmail(List<String> receivers, String subject, String message) {
        logger.info("(Service) Sending emails to: {}", receivers);

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        String[] receiverArray = receivers.toArray(new String[0]);
        simpleMailMessage.setTo(receiverArray);

        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);
        simpleMailMessage.setFrom(senderEmailId);
        mailSender.send(simpleMailMessage);

        logger.info("Emails have been sent.");
    }


    @Override
    public void sendEmailWithHTML(String receiver, String subject, String htmlContent) {
        logger.info("(Service) Sending email to: {} with html-content.", receiver);

        MimeMessage message = mailSender.createMimeMessage();

        try{
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(receiver);
            helper.setSubject(subject);
            helper.setFrom(senderEmailId);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            logger.info("Email has been sent with HTML content..");
        }
        catch (MessagingException messagingException){
            logger.error("Error while sending email with HTML content: ", messagingException);
        }
    }

    @Override
    public void sendEmailWithFile(String receiver, String subject, String message, File file) {
        logger.info("(Service) Sending email to: {} with file attachment.", receiver);

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(senderEmailId);
            helper.setTo(receiver);
            helper.setSubject(subject);
            helper.setText(message);

            FileSystemResource fileSystemResource = new FileSystemResource(file);
            helper.addAttachment(fileSystemResource.getFilename(), file);

            mailSender.send(mimeMessage);
            logger.info("Email send to user with file attachment");

        } catch (MessagingException messagingException) {
            logger.error("Error while sending email with file attachment: ", messagingException);
        }
    }

    @Override
    public void sendEmailWithFile(String[] receivers, String subject, String message, File file) {
        logger.info("(Service) Sending emails to: {} with file attachment.", (Object) receivers);

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(senderEmailId);
            helper.setTo(receivers);
            helper.setSubject(subject);
            helper.setText(message);

            FileSystemResource fileSystemResource = new FileSystemResource(file);
            helper.addAttachment(fileSystemResource.getFilename(), file);

            mailSender.send(mimeMessage);
            logger.info("Email send to user with file attachment");

        } catch (MessagingException messagingException) {
            logger.error("Error while sending email with file attachment: ", messagingException);
        }
    }

    @Override
    public void sendEmailWithHTML(String[] recipient, String subject, String htmlContent) {
        logger.info("(Service) Sending emails to: {} with html-content.", (Object) recipient);
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setFrom(senderEmailId);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            logger.info("Emails have been sent with HTML content..");

        } catch (MessagingException messagingException) {
            logger.error("Error while sending email with HTML content: ", messagingException);
        }
    }

    @Override
    public void sendEmailWithFileStream(String[] recipient, String subject, String message, MultipartFile file) {
        logger.info("(Service) Sending email to: {} with file attachment.", recipient);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(senderEmailId);
            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setText(message, true);

            helper.addAttachment(file.getOriginalFilename(), file);

            mailSender.send(mimeMessage);
            logger.info("Email sent to user with file attachment");
        } catch (MessagingException e) {
            logger.error("Messaging error while sending email: ", e);
            throw new RuntimeException("Email sending failed due to messaging error.", e);
        } catch (Exception e) {
            logger.error("Unexpected error while sending email: ", e);
            throw new RuntimeException("Email sending failed due to an unexpected error.", e);
        }
    }
}
