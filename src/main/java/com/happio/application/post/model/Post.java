package com.happio.application.post.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

public class Post {
    @NotNull
    private int id;
    @NotNull
    private int channelId;
    @NotNull
    private int creatorId;
    @NotBlank
    private String content;

    public Post(int id, int channelId, int creatorId, String content){
        this.id = id;
        this.channelId = channelId;
        this.creatorId = creatorId;
        this.content = content;
    }

    public int getId(){ return id; }
    public int getChannelId(){ return channelId; }
    public int getCreatorId(){ return creatorId; }
    public String getContent(){ return content; }
}