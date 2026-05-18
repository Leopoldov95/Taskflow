package com.taskflow.taskflow.service;

import com.taskflow.taskflow.dto.team.ManageTeamMemberRequest;
import com.taskflow.taskflow.dto.team.UpdateTeamRequest;
import com.taskflow.taskflow.entity.Team;
import com.taskflow.taskflow.entity.TeamMember;
import com.taskflow.taskflow.entity.User;

import java.util.List;

public interface TeamService {
    List<Team> findAll();
    Team findById(int id);
    Team save(Team team, int userId);
    Team updateTeam(int teamId, UpdateTeamRequest request);
    void deleteById(int id);
    // Team Member services
    List<TeamMember> getTeamMembers(int teamId);
    void addTeamMembers(int teamId, ManageTeamMemberRequest request);
    void removeTeamMembers(int teamId, ManageTeamMemberRequest request);
}
