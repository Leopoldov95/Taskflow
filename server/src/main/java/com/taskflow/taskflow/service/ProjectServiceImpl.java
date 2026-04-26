package com.taskflow.taskflow.service;

import com.taskflow.taskflow.dao.ProjectRepository;
import com.taskflow.taskflow.entity.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {
    private ProjectRepository projectRepository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    @Override
    public Project findById(int id) {
        Optional<Project> project = projectRepository.findById(id);

        Project theProject = null;

        if (project.isPresent()) {
            theProject = project.get();
        } else {
            throw new RuntimeException("Did not find Project with id: " + id);
        }

        return theProject;
    }

    @Override
    public Project save(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public void deleteById(int id) {
        projectRepository.deleteById(id);
    }
}
