package com.happio.application.post.controller;

import com.happio.application.post.model.Post;
import com.happio.application.post.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/post")
public class PostController{

    private PostService postService;

    public PostController (PostService postService) { this.postService = postService; }

    @PostMapping
    public ResponseEntity<String> createPost(@Valid @RequestBody Post request) {
        // We need to authenticate the use and check the users authorization
        // Currently anyone can make a post but we should check if the user exists and can make the post.
        postService.createPost(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Created"); // Return a better message
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPost(@PathVariable String postId){
        // As above, authentication and authorization needed
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPost(postId));
    }
}
