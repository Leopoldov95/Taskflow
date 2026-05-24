package com.taskflow.taskflow.service;

import com.taskflow.taskflow.dto.task.CreateTaskRequest;
import com.taskflow.taskflow.dto.task.UpdateTaskRequest;
import com.taskflow.taskflow.dto.taskcomment.CreateTaskCommentRequest;
import com.taskflow.taskflow.dto.taskcomment.UpdateTaskCommentRequest;
import com.taskflow.taskflow.entity.Task;
import com.taskflow.taskflow.entity.TaskComment;

import java.util.List;

public interface TaskService {
    List<Task> findAllByProjectId(int projectId);
    Task findById(int taskId);
    Task save(int projectId, CreateTaskRequest request);
    Task update(int taskId, UpdateTaskRequest request);
    void deleteById(int taskId);

    // task comments
    TaskComment saveComment(int taskId, CreateTaskCommentRequest request);
    TaskComment updateComment(int taskId, UpdateTaskCommentRequest request);
    void deleteComment(int taskCommentId);
}
