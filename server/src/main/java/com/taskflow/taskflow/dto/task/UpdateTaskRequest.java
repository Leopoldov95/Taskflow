package com.taskflow.taskflow.dto.task;

import com.taskflow.taskflow.entity.enums.TaskPriority;
import com.taskflow.taskflow.entity.enums.TaskStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class UpdateTaskRequest {
    @Size(min = 5, max = 100, message = "Title must be between 5 and 100 characters")
    private String title;

    @Size(max = 1500, message = "Description cannot exceed 1500 characters")
    private String description;

    private Integer assignee; // Here we want the User id as the DB accepts Integer

    private TaskPriority priority;

    private TaskStatus status;

    @FutureOrPresent
    private LocalDateTime dueDate;

    public UpdateTaskRequest() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAssignee() {
        return assignee;
    }

    public void setAssignee(Integer assignee) {
        this.assignee = assignee;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}
