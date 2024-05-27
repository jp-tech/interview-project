package com.happio.application.post.service;

import com.happio.application.post.model.Post;
import com.happio.application.post.repository.PostRepository;
import com.happio.application.task.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PostService {
    Logger logger = LoggerFactory.getLogger(PostService.class);

    private PostRepository postRepository;
    private TaskService taskService;

    public PostService (PostRepository postRepository, TaskService taskService) {
        this.postRepository = postRepository;
        this.taskService = taskService;
    }

    public void createPost(Post post) {
        int postId = post.getId();
        if(!isRequestParamsValid(post)){
            logger.error("Request is invalid for quote {}", postId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid data");
        }
        postRepository.create(post);
        taskService.createSendEmailNotificationsTask(postId);
    }

    public Post getPost(String postId) {
        return postRepository.getPostById(postId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found")
        );
    }

    private Boolean isRequestParamsValid(Post post){
        // TODO: Look in datastore to see if the channel and creator exist. Return true for now...
        return true;
    }
}
