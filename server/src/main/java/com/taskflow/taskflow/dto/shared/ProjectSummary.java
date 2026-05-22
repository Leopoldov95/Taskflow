package com.taskflow.taskflow.dto.shared;

import com.taskflow.taskflow.entity.Project;

public class ProjectSummary {
    private int id;
    private String name;
    private String projectKey;

    public ProjectSummary(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.projectKey = project.getProjectKey();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProjectKey() {
        return projectKey;
    }
}
