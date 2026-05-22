package com.taskflow.taskflow.rest;

import com.taskflow.taskflow.dto.task.CreateTaskRequest;
import com.taskflow.taskflow.dto.task.TaskResponse;
import com.taskflow.taskflow.entity.Task;
import com.taskflow.taskflow.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskRestController {

    private final TaskService taskService;

    @Autowired
    public TaskRestController(TaskService taskService) {
        this.taskService = taskService;
    }

    // get all tasks for a project
    // NOTE - May want to limit to a certain amount (e.g. 50 tasks) then load more on request
    @GetMapping("/projects/{projectId}/tasks")
    public List<TaskResponse> tasks(@PathVariable int projectId) {
        return taskService.findAllByProjectId(projectId)
                .stream()
                .map(TaskResponse::new)
                .toList();
    }

    // get single task
    @GetMapping("/tasks/{taskId}")
    public TaskResponse task(@PathVariable int taskId) {
        Task task = taskService.findById(taskId);
        return new TaskResponse(task);
    }

    // create a new task
    @PostMapping("projects/{projectId}/tasks")
    public TaskResponse saveTask(@PathVariable int projectId, @Valid @RequestBody CreateTaskRequest request) {
        Task taskDb = taskService.save(projectId, request);
        return new TaskResponse(taskDb);
    }
}
