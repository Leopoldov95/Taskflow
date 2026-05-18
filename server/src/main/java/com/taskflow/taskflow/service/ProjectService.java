package com.taskflow.taskflow.service;
import com.taskflow.taskflow.dto.project.CreateProjectRequest;
import com.taskflow.taskflow.dto.project.UpdateProjectRequest;
import com.taskflow.taskflow.entity.Project;
import com.taskflow.taskflow.entity.User;

import java.util.List;

public interface ProjectService {
    List<Project> findAllByTeamId(int teamId);
    Project findById(int id);
    Project save(int teamId, CreateProjectRequest project);
    Project update(int projectId, UpdateProjectRequest request);
    void deleteById(int id);
}
