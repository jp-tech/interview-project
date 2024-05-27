package com.happio.application.email.controller;

import com.happio.application.email.model.SentEmail;
import com.happio.application.email.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/email")
public class EmailController{

    private EmailService emailService;

    public EmailController (EmailService emailService) { this.emailService = emailService; }

    @GetMapping("/{emailId}")
    public ResponseEntity<SentEmail> getEmailById(@PathVariable String emailId){
        // As above, authentication and authorization needed
        return ResponseEntity.status(HttpStatus.OK).body(emailService.getSentEmail(emailId));
    }

}