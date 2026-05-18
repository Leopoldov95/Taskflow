package com.taskflow.taskflow.rest;

import com.taskflow.taskflow.dto.team.*;
import com.taskflow.taskflow.entity.Team;
import com.taskflow.taskflow.entity.User;
import com.taskflow.taskflow.service.TeamService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TeamRestController {

    private TeamService teamService;

    @Autowired
    public TeamRestController(TeamService teamService) {
        this.teamService = teamService;
    }

    // expose "/teams" and get a list of users
    @GetMapping("/teams")
    public List<TeamResponse> getTeams() {
        return teamService.findAll()
                .stream()
                .map(TeamResponse::new)
                .toList();
    }

    // Find single team by id
    @GetMapping("/teams/{teamId}")
    public TeamResponse getTeam(@PathVariable int teamId) {
        Team theTeam = teamService.findById(teamId);
        return new TeamResponse(theTeam);
    }

    // Create a new Team
    @PostMapping("/teams")
    public TeamResponse createTeam(
            @Valid @RequestBody CreateTeamRequest request,
            @AuthenticationPrincipal User currentUser) {
        // We need the user ID for this to function, as it can only be invoked by an authorized user, can just use requester's ID
        // TODO ~ This might be redundant as Spring Security layer will handle unauthorized users
        if (currentUser == null) {
            throw new RuntimeException("User not logged in");
        }

        Team team = new Team();
        team.setName(request.getName());
        team.setDescription(request.getDescription());
        team.setColor(request.getColor());
        team.setIcon(request.getIcon());

        Team dbTeam = teamService.save(team, currentUser.getId());
        return new TeamResponse(dbTeam);
    }

    // update (Patch) as existing Team
    @PatchMapping("/teams/{teamId}")
    public TeamResponse updateTeam(@PathVariable int teamId,
                           @Valid @RequestBody UpdateTeamRequest request
    ) {
            Team dbTeam = teamService.updateTeam(teamId, request);
            return new TeamResponse(dbTeam);
    }

    // delete team by id
    @DeleteMapping("/teams/{teamId}")
    public String deleteTeam(@PathVariable int teamId) {
        teamService.deleteById(teamId);
        return "Team deleted: " + teamId;
    }

    //////////////////////////////////////
    /// TEAM MEMBERS ////////////////////
    /////////////////////////////////////

    // Get all team members
    // We want a list of id, firstname, lastname, role
    @GetMapping("/teams/{teamId}/members")
    public List<TeamMemberResponse> getTeamMembers(@PathVariable int teamId) {
        return teamService.getTeamMembers(teamId)
                .stream()
                .map(TeamMemberResponse::new)
                .toList();
    }

    // Add new team member
    @PostMapping("/teams/{teamId}/members")
    public ResponseEntity<String> addTeamMember(
            @PathVariable int teamId,
            @RequestBody ManageTeamMemberRequest request) {
        teamService.addTeamMembers(teamId, request);
        return ResponseEntity.ok("Team members added successfully!");
    }

    // Remove (Delete) team member(s)
    @DeleteMapping("/teams/{teamId}/members")
    public ResponseEntity<String> deleteTeamMember(
            @PathVariable int teamId,
            @RequestBody ManageTeamMemberRequest request
    ) {
        teamService.removeTeamMembers(teamId, request);
        return ResponseEntity.ok("Team members deleted successfully!");
    }

}
