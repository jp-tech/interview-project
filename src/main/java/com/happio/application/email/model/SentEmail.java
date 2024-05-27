package com.happio.application.email.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SentEmail {
    @NotNull
    private int id;
    @NotBlank
    private String sentTo;
    @NotBlank
    private String sentFrom;
    @NotBlank
    private String emailType;

    public SentEmail(int id, String sentTo, String sentFrom, String emailType){
        this.id = id;
        this.sentTo = sentTo;
        this.sentFrom = sentFrom;
        this.emailType = emailType;
    }

    public int getId(){ return id; }
    public String getSentTo(){ return sentTo; }
    public String getSentFrom(){ return sentFrom; }
    public String getEmailType(){ return emailType; }
}