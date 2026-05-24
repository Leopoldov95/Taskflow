package com.taskflow.taskflow.dto.project;

import com.taskflow.taskflow.entity.Project;
import com.taskflow.taskflow.entity.enums.ProjectStatus;

import java.time.LocalDateTime;

// what data we want from the project via our API
public class ProjectResponse {
    private int id;
    private String name;
    private String description;
    private int teamId;
    private LocalDateTime updatedAt;
    private ProjectStatus status;
    private String projectKey;

    // constructor
    public ProjectResponse(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.description = project.getDescription();
        this.teamId = project.getTeam().getId();
        this.updatedAt = project.getUpdatedAt();
        this.status = project.getStatus();
        this.projectKey = project.getProjectKey();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getTeamId() {
        return teamId;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public String getProjectKey() {
        return projectKey;
    }
}
