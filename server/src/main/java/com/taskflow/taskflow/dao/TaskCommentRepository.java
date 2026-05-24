package com.taskflow.taskflow.dao;

import com.taskflow.taskflow.entity.TaskComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskCommentRepository extends JpaRepository<TaskComment, Integer> {
    // custom queries
    List<TaskComment> findByTaskId(int taskId);
}
