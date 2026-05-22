package com.taskflow.taskflow.dto.shared;

import com.taskflow.taskflow.entity.Team;

public class TeamSummary {
    private int id;
    private String name;
    private String color;
    private String icon;

    public TeamSummary(Team team) {
        this.id = team.getId();
        this.name = team.getName();
        this.color = team.getColor();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public String getIcon() {
        return icon;
    }
}
