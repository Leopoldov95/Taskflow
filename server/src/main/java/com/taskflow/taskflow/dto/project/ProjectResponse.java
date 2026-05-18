package com.taskflow.taskflow.dto.project;

import com.taskflow.taskflow.entity.Project;
import com.taskflow.taskflow.entity.Team;
import com.taskflow.taskflow.entity.enums.ProjectStatus;

import java.util.Date;

// what data we want from the project via our API
public class ProjectResponse {
    private int id;
    private String name;
    private String description;
    private Team team;
    private Date updatedAt;
    private ProjectStatus status;
    private String projectKey;

    // constructor
    public ProjectResponse(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.description = project.getDescription();
        this.team = project.getTeam();
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

    public Team getTeam() {
        return team;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public String getProjectKey() {
        return projectKey;
    }
}
