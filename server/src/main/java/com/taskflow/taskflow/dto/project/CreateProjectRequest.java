package com.taskflow.taskflow.dto.project;

import com.taskflow.taskflow.entity.Team;
import com.taskflow.taskflow.entity.enums.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Date;

// details we want from Users to create a Projet

public class CreateProjectRequest {

    @NotBlank(message = "Project name is required")
    @Size(min = 5, max = 50, message = "Project name must be between 5 and 50 characters")
    private String name;

    @NotBlank(message = "Description is required")
    @Size(min = 20, max = 300, message = "Description must be between 20 and 300 characters")
    private String description;

    @NotBlank(message = "Project key is required")
    @Size(min = 4, max = 4, message = "Project key must be exactly 4 characters")
    private String projectKey;

    public CreateProjectRequest() {}

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

}
