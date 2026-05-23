package com.taskflow.taskflow.service;

import com.taskflow.taskflow.dto.task.CreateTaskRequest;
import com.taskflow.taskflow.dto.task.UpdateTaskRequest;
import com.taskflow.taskflow.entity.Task;

import java.util.List;

public interface TaskService {
    List<Task> findAllByProjectId(int projectId);
    Task findById(int taskId);
    Task save(int projectId, CreateTaskRequest request);
    Task update(int taskId, UpdateTaskRequest request);
    void deleteById(int taskId);
}
