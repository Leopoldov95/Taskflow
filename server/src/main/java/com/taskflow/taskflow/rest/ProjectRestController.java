package com.taskflow.taskflow.rest;

import com.taskflow.taskflow.dto.project.CreateProjectRequest;
import com.taskflow.taskflow.dto.project.ProjectResponse;
import com.taskflow.taskflow.dto.project.UpdateProjectRequest;
import com.taskflow.taskflow.entity.Project;
import com.taskflow.taskflow.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public List<ProjectResponse> projects(@PathVariable int teamId) {
        return projectService.findAllByTeamId(teamId).stream().map(ProjectResponse::new).toList();
    }

    // get a single project by id
    @GetMapping("/projects/{projectId}")
    public ProjectResponse getProject(@PathVariable int projectId) {
        Project theProject = projectService.findById(projectId);

        return new ProjectResponse(theProject);
    }

    // Create a new project
    @PostMapping("/teams/{teamId}/projects")
    public ProjectResponse createProject(@PathVariable int teamId, @Valid @RequestBody CreateProjectRequest project) {
        Project dbProject = projectService.save(teamId, project);
        return new ProjectResponse(dbProject);
    }

    // update (PATCH) an existing project
    @PatchMapping("/projects/{projectId}")
    public ProjectResponse updateProject(@PathVariable int projectId, @Valid @RequestBody UpdateProjectRequest project) {
        Project dbProject = projectService.update(projectId, project);
        return new ProjectResponse(dbProject);
    }

    // delete project by id
    @DeleteMapping("/projects/{projectId}")
    public ResponseEntity<String> deleteProject(@PathVariable int projectId) {
        projectService.deleteById(projectId);
        return ResponseEntity.ok("Deleted project successfully with id: " + projectId);
    }
}
