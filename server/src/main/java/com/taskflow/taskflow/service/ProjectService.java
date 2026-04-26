package com.taskflow.taskflow.service;
import com.taskflow.taskflow.entity.Project;

import java.util.List;

public interface ProjectService {
    List<Project> findAll();
    Project findById(int id);
    Project save(Project project);
    void deleteById(int id);
}
