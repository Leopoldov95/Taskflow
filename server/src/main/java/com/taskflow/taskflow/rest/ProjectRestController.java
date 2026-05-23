package com.taskflow.taskflow.rest;

import com.taskflow.taskflow.dto.project.CreateProjectRequest;
import com.taskflow.taskflow.dto.project.ProjectResponse;
import com.taskflow.taskflow.dto.project.UpdateProjectRequest;
import com.taskflow.taskflow.entity.Project;
import com.taskflow.taskflow.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<ProjectResponse>> projects(@PathVariable int teamId) {
        List<ProjectResponse> response = projectService.findAllByTeamId(teamId).stream().map(ProjectResponse::new).toList();
        return ResponseEntity.ok(response);
    }

    // get a single project by id
    @GetMapping("/projects/{projectId}")
    public ResponseEntity<ProjectResponse> getProject(@PathVariable int projectId) {
        Project theProject = projectService.findById(projectId);
        return new ResponseEntity<>(new ProjectResponse(theProject), HttpStatus.OK);
    }

    // Create a new project
    @PostMapping("/teams/{teamId}/projects")
    public ResponseEntity<ProjectResponse> createProject(@PathVariable int teamId, @Valid @RequestBody CreateProjectRequest project) {
        Project dbProject = projectService.save(teamId, project);
        return new ResponseEntity<>(new ProjectResponse(dbProject), HttpStatus.CREATED);
    }

    // update (PATCH) an existing project
    @PatchMapping("/projects/{projectId}")
    public ResponseEntity<ProjectResponse> updateProject(@PathVariable int projectId, @Valid @RequestBody UpdateProjectRequest project) {
        Project dbProject = projectService.update(projectId, project);
        return new ResponseEntity<>(new ProjectResponse(dbProject), HttpStatus.OK);
    }

    // delete project by id
    @DeleteMapping("/projects/{projectId}")
    public ResponseEntity<String> deleteProject(@PathVariable int projectId) {
        projectService.deleteById(projectId);
        return ResponseEntity.ok("Deleted project successfully with id: " + projectId);
    }
}
