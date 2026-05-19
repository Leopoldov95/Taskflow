package com.taskflow.taskflow.dto.project;

import com.taskflow.taskflow.entity.Team;
import com.taskflow.taskflow.entity.enums.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UpdateProjectRequest {
    @Size(min = 5, max = 50, message = "Project name must be between 5 and 50 characters")
    private String name;

    @Size(min = 20, max = 300, message = "Description must be between 20 and 300 characters")
    private String description;

    @Size(min = 4, max = 4, message = "Project key must be exactly 4 characters")
    private String projectKey;

    private ProjectStatus status;

    public UpdateProjectRequest() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }
}
