package com.taskflow.taskflow.service;

import com.taskflow.taskflow.dto.team.AddTeamMemberRequest;
import com.taskflow.taskflow.dto.team.CreateTeamRequest;
import com.taskflow.taskflow.dto.team.UpdateTeamRequest;
import com.taskflow.taskflow.entity.Team;
import com.taskflow.taskflow.entity.TeamMember;
import com.taskflow.taskflow.entity.User;

import java.util.List;

public interface TeamService {
    List<Team> findAll();
    Team findById(int id);
    Team save(Team team, int userId);
    Team updateTeam(User user, int teamId, UpdateTeamRequest request);
    void deleteById(int id);
    // Team Member services
    List<TeamMember> getTeamMembers(int teamId, User currentUser);
    void addTeamMembers(int teamId, AddTeamMemberRequest request, User currentUser);
}
