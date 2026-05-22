package com.taskflow.taskflow.dao;

import com.taskflow.taskflow.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    // custom JPA logic
    List<Task> findAllByProjectId(int projectId);

    // custom query to get correct Task Key
    @Query("SELECT COALESCE(MAX(t.taskKey), 0) FROM Task t WHERE t.project.id = :projectId")
    int findMaxTaskKeyByProjectId(@Param("projectId") int projectId);
}
