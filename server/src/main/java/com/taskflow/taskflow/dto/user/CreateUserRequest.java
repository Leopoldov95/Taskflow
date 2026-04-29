package com.taskflow.taskflow.dto.user;

// DTO to handle the user creation request from the client side
// prevents client or API from sending properties like id, isActive, role, etc
public class CreateUserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    // for purpose of this project, role can be determined on user creation
    // Using a String for now for easier testing via postman and API tools
    private String role;

    public CreateUserRequest(){}

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
