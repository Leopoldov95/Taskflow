package com.taskflow.taskflow.dto.team;

import com.taskflow.taskflow.entity.Team;

import java.util.Date;

public class TeamResponse {
    private int id;
    private String name;
    private String description;
    private String color;
    private String icon;
    private boolean active;
    private Date createdAt;
    private int createdBy; // just the ID

    // constructor
    public TeamResponse(Team team) {
        this.id = team.getId();
        this.name = team.getName();
        this.description = team.getDescription();
        this.color = team.getColor();
        this.icon = team.getIcon();
        this.active = team.isActive();
        this.createdAt = team.getCreatedAt();
        this.createdBy = team.getCreatedBy().getId();
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

    public String getColor() {
        return color;
    }

    public String getIcon() {
        return icon;
    }

    public boolean isActive() {
        return active;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public int getCreatedBy() {
        return createdBy;
    }
}
