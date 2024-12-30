package com.reonfernandes.EmailApplication.controller;

import com.reonfernandes.EmailApplication.model.Email;
import com.reonfernandes.EmailApplication.service.impl.EmailServiceImpl;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@CrossOrigin("*")
@Controller
public class EmailController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final EmailServiceImpl emailService;

    public EmailController(EmailServiceImpl emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/")
    public String accessMailPage(Model model){
        logger.info("(Controller) Accessing Swift Mailer page.");
        Email emailForm = new Email();
        model.addAttribute("email", emailForm);
        model.addAttribute("sending", false);
        return "swift-mailer";
    }

    @PostMapping("sendEmail")
    public String sendEmail(@Valid @ModelAttribute("email") Email email, BindingResult result, Model model) {
        logger.info("(Controller) Sending Email");

        if (result.hasErrors()) {
            model.addAttribute("sending", false);
            return "swift-mailer";
        }

        try {
            model.addAttribute("sending", true);
            logger.info("Sending attribute set to true in the model");


            if (email.getFile() != null && !email.getFile().isEmpty()){
                logger.info("Sending to single recipient with file.");
                // can send email to single and multiple users with file and html content or with simple text.
                emailService.sendEmailWithFileStream(
                        email.getRecipient(), email.getSubject(), email.getMessage(), email.getFile()
                );
            }
            else {
                logger.info("Sending to single recipient without file.");
                // can send email to single and multiple users with html content and simple text.
                emailService.sendEmailWithHTML(
                        email.getRecipient(), email.getSubject(), email.getMessage()
                );
            }

            logger.info("Email sent successfully!");
            model.addAttribute("success", "Email sent successfully.");
            return "success";
        }
        catch (Exception e) {
            logger.error("Error occurred while sending email: ", e);
            model.addAttribute("error", "Failed to send email. Please try again later.");
            model.addAttribute("sending", false);
            return "redirect:/error";
        }
    }
}