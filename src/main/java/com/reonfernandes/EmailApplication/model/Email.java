package com.reonfernandes.EmailApplication.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Email {
    @NotBlank(message = "Receiver's email is required.")
    private String receiver;
    @NotBlank(message = "Subject of email is required.")
    private String subject;
    @NotBlank(message = "Please compose message.")
    private String message;
    private MultipartFile file;
}
