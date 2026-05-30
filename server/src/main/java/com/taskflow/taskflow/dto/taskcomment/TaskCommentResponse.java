package com.taskflow.taskflow.dto.taskcomment;

import com.taskflow.taskflow.dto.shared.UserSummary;
import com.taskflow.taskflow.entity.TaskComment;
import com.taskflow.taskflow.entity.User;

import java.time.LocalDateTime;

public class TaskCommentResponse {
    private int id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserSummary createdBy;

    public TaskCommentResponse(TaskComment taskComment) {
        this.id = taskComment.getId();
        this.createdAt = taskComment.getCreatedAt();
        this.updatedAt = taskComment.getUpdatedAt();
        this.content = taskComment.getContent();
        this.createdBy = new UserSummary(taskComment.getCreatedBy());
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public UserSummary getCreatedBy() {
        return createdBy;
    }

    public int getId() { return id; }
}
