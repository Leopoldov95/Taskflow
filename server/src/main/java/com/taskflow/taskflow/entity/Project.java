package com.taskflow.taskflow.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "projects")
public class Project {
    // define fields

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="description")
    private String description;

    @Column(name="status")
    // ENUM type
    private String status;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name="updated_at")
    private Date updatedAt;

    @Column(name="team_id")
    // Need to add foreign key constraint
    private int teamId;

    @Column(name="project_key")
    private int projectKey;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(int projectKey) {
        this.projectKey = projectKey;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", teamId=" + teamId +
                ", projectKey=" + projectKey +
                '}';
    }
}
