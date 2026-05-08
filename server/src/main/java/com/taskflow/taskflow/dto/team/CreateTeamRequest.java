package com.taskflow.taskflow.dto.team;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CreateTeamRequest {

    @NotBlank(message = "Team name is required")
    @Size(min = 5, max = 50, message = "Team name must be between 5 and 50 characters")
    private String name;

    @NotBlank(message = "Description is required")
    @Size(min = 20, max = 500, message = "Description must be between 20 and 500 characters")
    private String description;

    // Must be a hex color value
    @NotBlank
    @Pattern(
            regexp = "^#[0-9a-fA-F]{6}$",
            message = "Invalid hex color"
    )
    private String color;

    @NotBlank(message = "Icon is required")
    @Size(min = 3, max = 20, message = "Must be a valid icon")
    private String icon;

    public CreateTeamRequest() {}

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
