package com.taskflow.taskflow.dto.user;

public class UserResponse {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private boolean isActive;

    public UserResponse() {}

    public UserResponse(int id, String firstName, String lastName, String email, boolean isActive) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.isActive = isActive;
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

    public String getEmail() {
        return email;
    }

    public boolean isActive() {
        return isActive;
    }
}
