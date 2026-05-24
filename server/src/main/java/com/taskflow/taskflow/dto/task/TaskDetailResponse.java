package com.taskflow.taskflow.dto.task;

import com.taskflow.taskflow.dto.taskcomment.TaskCommentResponse;
import com.taskflow.taskflow.entity.Task;

import java.util.List;

public class TaskDetailResponse extends TaskResponse {

    private List<TaskCommentResponse> comments;

    public TaskDetailResponse(Task task) {
        super(task);
        if (task.getComments() != null) {
            this.comments = task.getComments().stream()
                    .map(TaskCommentResponse::new)
                    .toList();
        }
    }

    public List<TaskCommentResponse> getComments() {
        return comments;
    }
}