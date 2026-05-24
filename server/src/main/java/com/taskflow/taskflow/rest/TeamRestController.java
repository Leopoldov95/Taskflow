package com.taskflow.taskflow.rest;

import com.taskflow.taskflow.dto.team.*;
import com.taskflow.taskflow.entity.Team;
import com.taskflow.taskflow.entity.User;
import com.taskflow.taskflow.service.TeamService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teams")
public class TeamRestController {

    private TeamService teamService;

    @Autowired
    public TeamRestController(TeamService teamService) {
        this.teamService = teamService;
    }

    // expose "/teams" and get a list of users
    @GetMapping
    public ResponseEntity<List<TeamResponse>> getTeams() {
    List<TeamResponse> result = teamService.findAll()
                .stream()
                .map(TeamResponse::new)
                .toList();
    return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // Find single team by id
    @GetMapping("/{teamId}")
    public ResponseEntity<TeamResponse> getTeam(@PathVariable int teamId) {
        Team theTeam = teamService.findById(teamId);
       TeamResponse response = new TeamResponse(theTeam);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Create a new Team
    @PostMapping()
    public ResponseEntity<TeamResponse> createTeam(
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
        TeamResponse response = new TeamResponse(dbTeam);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // update (Patch) as existing Team
    @PatchMapping("/{teamId}")
    public ResponseEntity<TeamResponse> updateTeam(@PathVariable int teamId,
                           @Valid @RequestBody UpdateTeamRequest request
    ) {
            Team dbTeam = teamService.updateTeam(teamId, request);
            TeamResponse response = new TeamResponse(dbTeam);
            return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // delete team by id
    @DeleteMapping("/{teamId}")
    public ResponseEntity<String> deleteTeam(@PathVariable int teamId) {
        teamService.deleteById(teamId);
        return new ResponseEntity<>("Team deleted: " + teamId, HttpStatus.OK);
    }

    //////////////////////////////////////
    /// TEAM MEMBERS ////////////////////
    /////////////////////////////////////

    // Get all team members
    // We want a list of id, firstname, lastname, role
    @GetMapping("/{teamId}/members")
    public ResponseEntity<List<TeamMemberResponse>> getTeamMembers(@PathVariable int teamId) {
        List<TeamMemberResponse> response = teamService.getTeamMembers(teamId)
                .stream()
                .map(TeamMemberResponse::new)
                .toList();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Add new team member
    @PostMapping("/{teamId}/members")
    public ResponseEntity<String> addTeamMember(
            @PathVariable int teamId,
            @Valid @RequestBody ManageTeamMemberRequest request) {
        teamService.addTeamMembers(teamId, request);
        return ResponseEntity.ok("Team members added successfully!");
    }

    // Remove (Delete) team member(s)
    @DeleteMapping("/{teamId}/members")
    public ResponseEntity<String> deleteTeamMember(
            @PathVariable int teamId,
            @Valid @RequestBody ManageTeamMemberRequest request
    ) {
        teamService.removeTeamMembers(teamId, request);
        return ResponseEntity.ok("Team members deleted successfully!");
    }

}
