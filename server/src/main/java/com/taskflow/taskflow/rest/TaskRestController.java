package com.taskflow.taskflow.rest;

import com.taskflow.taskflow.dto.task.CreateTaskRequest;
import com.taskflow.taskflow.dto.task.TaskResponse;
import com.taskflow.taskflow.dto.task.UpdateTaskRequest;
import com.taskflow.taskflow.entity.Task;
import com.taskflow.taskflow.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<TaskResponse>> tasks(@PathVariable int projectId) {
        List<TaskResponse> response = taskService.findAllByProjectId(projectId)
                .stream()
                .map(TaskResponse::new)
                .toList();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // get single task
    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<TaskResponse> task(@PathVariable int taskId) {
        Task task = taskService.findById(taskId);
        return new ResponseEntity<>(new TaskResponse(task), HttpStatus.OK);
    }

    // create a new task
    @PostMapping("projects/{projectId}/tasks")
    public ResponseEntity<TaskResponse> saveTask(@PathVariable int projectId, @Valid @RequestBody CreateTaskRequest request) {
        Task taskDb = taskService.save(projectId, request);
        return new ResponseEntity<>(new TaskResponse(taskDb), HttpStatus.CREATED);
    }

    // Update a task
    @PatchMapping("/tasks/{taskId}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable int taskId, @Valid @RequestBody UpdateTaskRequest request) {
        Task taskDb = taskService.update(taskId, request);
        return new ResponseEntity<>(new TaskResponse(taskDb), HttpStatus.OK);
    }

    // Remove a task
    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable int taskId) {
        taskService.deleteById(taskId);
        return new ResponseEntity<>("Task Deleted" + taskId, HttpStatus.OK);
    }

}
