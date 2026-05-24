package com.taskflow.taskflow.rest;

import com.taskflow.taskflow.dto.task.CreateTaskRequest;
import com.taskflow.taskflow.dto.task.TaskDetailResponse;
import com.taskflow.taskflow.dto.task.TaskResponse;
import com.taskflow.taskflow.dto.task.UpdateTaskRequest;
import com.taskflow.taskflow.dto.taskcomment.CreateTaskCommentRequest;
import com.taskflow.taskflow.dto.taskcomment.TaskCommentResponse;
import com.taskflow.taskflow.dto.taskcomment.UpdateTaskCommentRequest;
import com.taskflow.taskflow.entity.Task;
import com.taskflow.taskflow.entity.TaskComment;
import com.taskflow.taskflow.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TaskRestController {

    private final TaskService taskService;

    @Autowired
    public TaskRestController(TaskService taskService) {
        this.taskService = taskService;
    }

    // get all tasks for a project
    @GetMapping("/projects/{projectId}/tasks")
    public ResponseEntity<Page<TaskResponse>> tasks(
            @PathVariable int projectId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        Page<TaskResponse> response = taskService.findAllByProjectId(projectId, page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // get single task
    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<TaskDetailResponse> task(@PathVariable int taskId) {
        Task task = taskService.findById(taskId);
        return new ResponseEntity<>(new TaskDetailResponse(task), HttpStatus.OK);
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
        return new ResponseEntity<>("Task Deleted: " + taskId, HttpStatus.OK);
    }

    //------------------
    // TASK COMMENTS
    //-----------------

    // create a new comment
    @PostMapping("/tasks/{taskId}/comments")
    public ResponseEntity<TaskCommentResponse> saveTaskComment(@PathVariable int taskId,
                                                               @Valid @RequestBody CreateTaskCommentRequest request) {
        TaskComment taskComment = taskService.saveComment(taskId, request);
        return new ResponseEntity<>(new TaskCommentResponse(taskComment), HttpStatus.CREATED);
    }

    // update an existing comment
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<TaskCommentResponse> updateTaskComment(@PathVariable int commentId,
                                                                 @Valid @RequestBody UpdateTaskCommentRequest request) {
        TaskComment taskComment = taskService.updateComment(commentId, request);
        return new ResponseEntity<>(new TaskCommentResponse(taskComment), HttpStatus.OK);
    }

    // delete a comment
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<String> deleteTaskComment(@PathVariable int commentId) {
        taskService.deleteComment(commentId);
        return new ResponseEntity<>("Comment Deleted: " + commentId, HttpStatus.OK);
    }

}
