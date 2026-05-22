package com.taskflow.taskflow.dto.task;

import com.taskflow.taskflow.dto.shared.ProjectSummary;
import com.taskflow.taskflow.dto.shared.TeamSummary;
import com.taskflow.taskflow.dto.shared.UserSummary;
import com.taskflow.taskflow.entity.Task;
import com.taskflow.taskflow.entity.enums.TaskPriority;
import com.taskflow.taskflow.entity.enums.TaskStatus;

import java.time.LocalDateTime;
import java.util.Date;

public class TaskResponse {

    private int id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int taskKey;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDateTime dueDate;
    private LocalDateTime deletedAt;
    private ProjectSummary project;
    private UserSummary assignee;
    private UserSummary createdBy;
    private TeamSummary team;

    public TaskResponse(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.createdAt =  task.getCreatedAt();
        this.updatedAt = task.getUpdatedAt();
        this.taskKey = task.getTaskKey();
        this.status = task.getStatus();
        this.priority = task.getPriority();
        this.dueDate = task.getDueDate();
        this.deletedAt = task.getDeletedAt();
        this.project = new ProjectSummary(task.getProject());
        // need to handle cases where assignee is null
        this.assignee = task.getAssignee() != null
                ? new UserSummary(task.getAssignee())
                : null;        this.createdBy = new UserSummary(task.getCreatedBy());
        this.team = new TeamSummary(task.getTeam());
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public int getTaskKey() {
        return taskKey;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public ProjectSummary getProject() {
        return project;
    }

    public UserSummary getAssignee() {
        return assignee;
    }

    public UserSummary getCreatedBy() {
        return createdBy;
    }

    public TeamSummary getTeam() {
        return team;
    }
}
