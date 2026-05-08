package com.taskflow.taskflow.rest;

import com.taskflow.taskflow.dto.team.*;
import com.taskflow.taskflow.entity.Team;
import com.taskflow.taskflow.entity.User;
import com.taskflow.taskflow.exception.ResourceNotFoundException;
import com.taskflow.taskflow.security.UserDetailsServiceImpl;
import com.taskflow.taskflow.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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

        if (theTeam == null) {
            throw new RuntimeException("Team not found: " + teamId);
        }

        return new TeamResponse(theTeam);
    }

    // Create a new Team
    @PostMapping("/teams")
    public TeamResponse createTeam(
            @RequestBody Team theTeam,
            @AuthenticationPrincipal User currentUser) {
        // We need the user ID for this to function, as it can only be invoked by an authorized user, can just use requester's ID
        if (currentUser == null) {
            throw new RuntimeException("User not logged in");
        }

        Team dbTeam = teamService.save(theTeam, currentUser.getId());
        return new TeamResponse(dbTeam);
    }

    // update (Patch) as existing Team
    @PatchMapping("/teams")
    public Team updateTeam(@AuthenticationPrincipal User currentUser,
                           @PathVariable int teamId,
                           @RequestBody UpdateTeamRequest request
    ) {
            return teamService.updateTeam(currentUser, teamId, request);
    }

    // delete team by id
    @DeleteMapping("/teams/{teamId}")
    public String deleteTeam(@PathVariable int teamId) {
        Team tempTeam = teamService.findById(teamId);

        if (tempTeam == null) {
            throw new ResourceNotFoundException("Team not found: " + teamId);
        }

        teamService.deleteById(teamId);
        return "Team deleted: " + teamId;
    }

    //////////////////////////////////////
    /// TEAM MEMBERS ////////////////////
    /////////////////////////////////////

    // Get all team members
    // We want a list of id, firstname, lastname, role
    @GetMapping("/teams/{teamId}/members")
    public List<TeamMemberResponse> getTeamMembers(@PathVariable int teamId, @AuthenticationPrincipal User currentUser) {
        return teamService.getTeamMembers(teamId, currentUser)
                .stream()
                .map(TeamMemberResponse::new)
                .toList();
    }

    // Add new team member
    @PostMapping("/teams/{teamId}/members")
    public ResponseEntity<String> addTeamMember(
            @PathVariable int teamId,
            @RequestBody AddTeamMemberRequest request,
            @AuthenticationPrincipal User currentUser) {
        teamService.addTeamMembers(teamId, request, currentUser);
        return ResponseEntity.ok("Team members added successfully!");
    }

    // Remove (Delete) team member
}
