package com.taskflow.taskflow.dto.team;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UpdateTeamRequest {

    @Size(min = 5, max = 50, message = "Team name must be between 5 and 50 characters")
    private String name;

    @Size(min = 20, max = 500, message = "Description must be between 20 and 500 characters")
    private String description;

    private Boolean isActive;

    @Pattern(
            regexp = "^#[0-9a-fA-F]{6}$",
            message = "Invalid hex color"
    )
    private String color;

    @Size(min = 3, max = 20, message = "Must be a valid icon")
    private String icon;

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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
