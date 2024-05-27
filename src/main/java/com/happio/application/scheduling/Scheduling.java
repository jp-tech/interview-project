package com.happio.application.scheduling;

import com.happio.application.post.repository.PostRepository;
import com.happio.application.task.service.TaskService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduling {

    private TaskService taskService;

    public Scheduling (PostRepository postRepository, TaskService taskService) {
        this.taskService = taskService;
    }

    @Scheduled(fixedRateString = "${scheduler.interval.rate}")
    public void run() {
        taskService.handleTaskQueue();
    }
}
