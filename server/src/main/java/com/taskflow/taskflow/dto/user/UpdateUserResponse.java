package com.taskflow.taskflow.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;

public class UpdateUserResponse {

    private UserResponse user;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String token;

    public UpdateUserResponse(UserResponse user, String token) {
        this.user = user;
        this.token = token;
    }

    public UpdateUserResponse(UserResponse user) {
        this.user = user;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
