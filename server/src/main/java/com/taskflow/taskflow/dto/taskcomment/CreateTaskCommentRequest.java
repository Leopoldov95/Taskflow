package com.taskflow.taskflow.dto.taskcomment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateTaskCommentRequest {

    @NotBlank(message = "Content body cannot be empty")
    @Size(min = 3, max = 500, message = "Comment must be between 3 and 500 characters")
    private String content;

    public CreateTaskCommentRequest() {}

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
