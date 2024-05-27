package com.happio.application.task.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EmailTask {
    @NotNull
    private int id;
    @NotNull
    private int postId;
    @NotBlank
    private String status;

    public EmailTask(int id, int postId, String status){
        this.id = id;
        this.postId = postId;
        this.status = status;
    }

    public int getId(){ return id; }
    public int getPostId(){ return postId; }
    public String getContent(){ return status; }
}