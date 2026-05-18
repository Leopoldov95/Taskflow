package com.taskflow.taskflow.dao;

import com.taskflow.taskflow.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
    // so we'll never need a query to fetch all projects (for now)
    List<Project> findAllByTeamId(int teamId);
}
