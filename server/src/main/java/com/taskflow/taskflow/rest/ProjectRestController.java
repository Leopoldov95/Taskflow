package com.taskflow.taskflow.rest;

import com.taskflow.taskflow.entity.Project;
import com.taskflow.taskflow.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProjectRestController {

    private ProjectService projectService;

    @Autowired
    public ProjectRestController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // get a list of projects
    @GetMapping("/projects")
    public List<Project> projects() {
        return projectService.findAll();
    }

    // get a single project by id
    @GetMapping("/projects/{projectId}")
    public Project getProject(@PathVariable int projectId) {
        Project theProject = projectService.findById(projectId);

        if (theProject == null) {
            throw new RuntimeException("Project not found: " + projectId);
        }

        return theProject;
    }

    // Create a new project
    @PostMapping("/projects")
    public Project createProject(@RequestBody Project project) {
        Project dbProject = projectService.save(project);
        return dbProject;
    }

    // update (PUT) an existing project
    @PutMapping("/projects")
    public Project updateProject(@RequestBody Project project) {
        Project dbProject = projectService.save(project);
        return dbProject;
    }

    // delete project by id
    @DeleteMapping("/projects/{projectId}")
    public String deleteProject(@PathVariable int projectId) {
        Project tempProject = projectService.findById(projectId);

        if (tempProject == null) {
            throw new RuntimeException("Project not found: " + projectId);
        }

        projectService.deleteById(projectId);
        return "Project deleted: " + projectId;
    }
}
