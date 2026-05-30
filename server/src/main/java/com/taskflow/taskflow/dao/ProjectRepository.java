package com.taskflow.taskflow.dao;

import com.taskflow.taskflow.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
    // Will only fetch multiple projects when given team is
    List<Project> findAllByTeamId(int teamId);
    boolean existsByTeamIdAndProjectKey(int teamId, String projectKey);
}
