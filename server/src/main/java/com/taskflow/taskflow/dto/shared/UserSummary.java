package com.taskflow.taskflow.dto.shared;

/*
Helper DTO to get necessary User info to display on frontend
 */

import com.taskflow.taskflow.entity.User;

public class UserSummary {
    private int id;
    private String firstName;
    private String lastName;

    public UserSummary(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

}
