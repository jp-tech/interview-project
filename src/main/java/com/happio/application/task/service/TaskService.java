package com.happio.application.task.service;

import com.happio.application.email.service.EmailService;
import com.happio.application.post.service.PostService;
import com.happio.application.task.model.EmailTask;
import com.happio.application.task.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class TaskService {
    Logger logger = LoggerFactory.getLogger(PostService.class);

    @Value("${spring.application.url}")
    private String BASEURL;

    private TaskRepository taskRepository;
    private EmailService emailService;

    public TaskService(TaskRepository taskRepository, EmailService emailService) {
        this.taskRepository = taskRepository;
        this.emailService = emailService;
    }

    public void createSendEmailNotificationsTask(int postId)  {
        taskRepository.create(postId);
    }

    public String getPost(int postId){
        String url = BASEURL + "/post/" + postId;
        ResponseEntity<String> r = new RestTemplate().getForEntity(url, String.class);
        return r.getBody();
    }

    public void handleTaskQueue(){
        logger.info("Starting queue processing");
        List<EmailTask> tasks = taskRepository.getAndUpdateAwaitingTasks();

        int totalAwaitingTasks = tasks.size();
        if (totalAwaitingTasks == 0){
            logger.info("No tasks to process. Exiting...");
            return;
        }

        logger.info("Email tasks to process: {}", tasks.size());
        for (EmailTask task : tasks) {
            int taskId = task.getId();
            try {
                String s = getPost(task.getPostId());
                sendEmailsToSubscribers(s);
                handleSuccessfulTaskProcessing(taskId);
            } catch (Exception ex) {
                handleErrorProcessingTask(taskId, ex);
            }
        }
        logger.info("Queue processing complete");
    }

    private void sendEmailsToSubscribers(String post){
        // Get the subscribers to send to, this should come from user datastore or api
        // I've hard coded this but this should come from user datastore or api
        // Using ths post.channel_id, we should get the list of all his subscribers. e.g:
        // String[] emails = getChanelSubscribers(post.channel_id)
        String[] emails = {"oijo@gmail.com", "dnoin@icloud.com"};
        emailService.sendSubscriberEmails(Arrays.asList(emails));
    }

    private void handleErrorProcessingTask(int taskId, Exception ex){
        logger.error("Error performing email task: {}", ex.toString());
        taskRepository.setErrorStatus(taskId);
    }

    private void handleSuccessfulTaskProcessing(int taskId){
        logger.info("Successfully ran task performing email task {}", taskId);
        taskRepository.setSuccessStatus(taskId);
    }
}
