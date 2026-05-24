package com.taskflow.taskflow.dto.taskcomment;

import jakarta.validation.constraints.Size;

public class UpdateTaskCommentRequest {

    @Size(min = 3, max = 500, message = "Comment must be between 3 and 500 characters")
    private String content;

    public UpdateTaskCommentRequest() {}

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
