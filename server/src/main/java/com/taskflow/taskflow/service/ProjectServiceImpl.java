package com.taskflow.taskflow.service;

import com.taskflow.taskflow.dao.ProjectRepository;
import com.taskflow.taskflow.dao.TeamMemberRepository;
import com.taskflow.taskflow.dao.TeamRepository;
import com.taskflow.taskflow.dto.project.CreateProjectRequest;
import com.taskflow.taskflow.dto.project.UpdateProjectRequest;
import com.taskflow.taskflow.entity.Project;
import com.taskflow.taskflow.entity.Team;
import com.taskflow.taskflow.entity.TeamMember;
import com.taskflow.taskflow.entity.User;
import com.taskflow.taskflow.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {
    private ProjectRepository projectRepository;
    private TeamRepository teamRepository;
    private TeamMemberRepository teamMemberRepository;
    private TeamAccessService teamAccessService;
    private AuthService authService;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, TeamRepository teamRepository,
                              TeamMemberRepository teamMemberRepository,
                              TeamAccessService teamAccessService,
                              AuthService authService) {
        this.projectRepository = projectRepository;
        this.teamRepository = teamRepository;
        this.teamMemberRepository = teamMemberRepository;
        this.teamAccessService = teamAccessService;
        this.authService = authService;
    }

    @Override
    public List<Project> findAllByTeamId(int teamId) {
        User currentUser = authService.getCurrentUser();
        // Only Team Members or Owner can access Projects
        // this will aready throw an error, thus no need to conditionally check
        teamAccessService.validateTeamAccess(teamId, currentUser.getId());
        return projectRepository.findAllByTeamId(teamId);
    }

    @Override
    public Project findById(int id) {
        User currentUser = authService.getCurrentUser();

        Project project = projectRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Project not found with id: " + id));

        teamAccessService.validateTeamAccess(project.getTeam().getId(), currentUser.getId());

        return project;
    }

    @Override
    public Project save(int teamId, CreateProjectRequest project) {
        User currentUser = authService.getCurrentUser();

        // ensure user is owner of team
        teamAccessService.validateTeamAccess(teamId, currentUser.getId());

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found: " + teamId));

        Project newProject = new Project();
        newProject.setName(project.getName());
        newProject.setDescription(project.getDescription());
        newProject.setProjectKey(project.getProjectKey());
        newProject.setTeam(team);

        return projectRepository.save(newProject);
    }

    @Override
    public Project update(int projectId, UpdateProjectRequest request) {
        User currentUser = authService.getCurrentUser();

        // ensure user has access AND is a OWNER of the Team
        Project project = projectRepository.findById(projectId).orElseThrow(() ->
                new ResourceNotFoundException("Project not found with id: " + projectId));

        teamAccessService.validateOwnerAccess(project.getTeam().getId(), currentUser.getId());

        // checks are good, user is an OWNER and can update project details
        if (request.getName() != null) {project.setName(request.getName());}
        if (request.getDescription() != null) {project.setDescription(request.getDescription());}
        if (request.getProjectKey() != null) {project.setProjectKey(request.getProjectKey());}
        if (request.getStatus() != null) {project.setStatus(request.getStatus());}

        return projectRepository.save(project);
    }

    @Override
    public void deleteById(int id) {
        User currentUser = authService.getCurrentUser();

        // ensure user has access AND is a OWNER of the Team
        Project project = projectRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Project not found with id: " + id));

        teamAccessService.validateOwnerAccess(project.getTeam().getId(), currentUser.getId());

        projectRepository.deleteById(id);
    }
}
