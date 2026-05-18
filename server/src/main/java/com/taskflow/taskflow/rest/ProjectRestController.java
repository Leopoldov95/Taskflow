package com.taskflow.taskflow.rest;

import com.taskflow.taskflow.dto.project.CreateProjectRequest;
import com.taskflow.taskflow.dto.project.UpdateProjectRequest;
import com.taskflow.taskflow.entity.Project;
import com.taskflow.taskflow.entity.User;
import com.taskflow.taskflow.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    @GetMapping("/teams/{teamId}/projects")
    public List<Project> projects(@PathVariable int teamId) {
        return projectService.findAllByTeamId(teamId);
    }

    // get a single project by id
    @GetMapping("/projects/{projectId}")
    public Project getProject(@PathVariable int projectId) {
        Project theProject = projectService.findById(projectId);

        return theProject;
    }

    // Create a new project
    @PostMapping("/teams/{teamId}/projects")
    public Project createProject(@PathVariable int teamId, @RequestBody CreateProjectRequest project) {
        Project dbProject = projectService.save(teamId, project);
        return dbProject;
    }

    // update (PATCH) an existing project
    @PatchMapping("/projects/{projectId}")
    public Project updateProject(@PathVariable int projectId, @RequestBody UpdateProjectRequest project) {
        Project dbProject = projectService.update(projectId, project);
        return dbProject;
    }

    // delete project by id
    @DeleteMapping("/projects/{projectId}")
    public ResponseEntity<String> deleteProject(@PathVariable int projectId) {
        projectService.deleteById(projectId);
        return ResponseEntity.ok("Deleted project successfully with id: " + projectId);
    }
}
