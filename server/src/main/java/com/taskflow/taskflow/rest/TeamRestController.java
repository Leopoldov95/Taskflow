package com.taskflow.taskflow.rest;

import com.taskflow.taskflow.entity.Team;
import com.taskflow.taskflow.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Team> getTeams() {
        return teamService.findAll();
    }

    // Find single team by id
    @GetMapping("/teams/{teamId}")
    public Team getTeam(@PathVariable int teamId) {
        Team theTeam = teamService.findById(teamId);

        if (theTeam == null) {
            throw new RuntimeException("Team not found: " + teamId);
        }

        return theTeam;
    }

    // Create a new Team
    @PostMapping("/teams")
    public Team createTeam(@RequestBody Team theTeam) {
        Team dbTeam = teamService.save(theTeam);
        return dbTeam;
    }

    // update (PUT) as existing Team
    @PutMapping("/teams")
    public Team updateTeam(@RequestBody Team theTeam) {
        Team dbTeam = teamService.save(theTeam);
        return dbTeam;
    }

    // delete team by id
    @DeleteMapping("/teams/{teamId}")
    public String deleteTeam(@PathVariable int teamId) {
        Team tempTeam = teamService.findById(teamId);

        if (tempTeam == null) {
            throw new RuntimeException("Team not found: " + teamId);
        }

        teamService.deleteById(teamId);
        return "Team deleted: " + teamId;
    }
}
