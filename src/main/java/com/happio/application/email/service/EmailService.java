package com.happio.application.email.service;

import com.happio.application.email.model.EmailTypes;
import com.happio.application.email.model.SentEmail;
import com.happio.application.email.repository.EmailRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EmailService {

    // For ease, this is hardcoded in the class but this should not exist here.
    private String FROM_EMAIL = "hello@support.com";

    private EmailRepository emailRepository;

    public EmailService(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    public void sendSubscriberEmails(List<String> emails){
        // This functionality is blocking and runs one after another in sequence which can affect performance and speed of the application
        // It would be better to run these computations concurrently using multithreading or coroutines (like in kotlin)
        // to run these independent of one another
        for(String email : emails){
            // in a real life scenario, this sending of emails could fail. The errors should be captured in the data store
            sendEmailToUser(email, FROM_EMAIL, EmailTypes.NEW_CONTENT.toString());
        }
    }

    private void sendEmailToUser(String emailAddress, String fromAddress, String emailType){
        // thirdParty email service library to send emails here (AWS SES, Sendgrid etc)
        // EmailThirdParty.send(emailType, emailAddress, fromAddress)
        emailRepository.saveEmail(emailAddress, fromAddress, emailType);
    }

    public SentEmail getSentEmail(String email_id){
        return emailRepository.getSentEmail(Integer.parseInt(email_id)).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found") // I should collect all error messages in an error messages enum for consistency and ease of uses
        );
    }
}
