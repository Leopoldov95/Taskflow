package com.taskflow.taskflow.service;

import com.taskflow.taskflow.dao.TeamMemberRepository;
import com.taskflow.taskflow.dao.TeamRepository;
import com.taskflow.taskflow.dao.UserRepository;
import com.taskflow.taskflow.dto.team.ManageTeamMemberRequest;
import com.taskflow.taskflow.dto.team.UpdateTeamRequest;
import com.taskflow.taskflow.entity.Team;
import com.taskflow.taskflow.entity.TeamMember;
import com.taskflow.taskflow.entity.User;
import com.taskflow.taskflow.entity.enums.TeamRole;
import com.taskflow.taskflow.exception.DuplicateResourceException;
import com.taskflow.taskflow.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;


import java.util.List;
import java.util.Optional;

@Service
public class TeamServiceImpl implements TeamService {
    private TeamRepository teamRepository;
    private UserRepository userRepository;
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository, UserRepository userRepository, TeamMemberRepository teamMemberRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.teamMemberRepository = teamMemberRepository;
    }

    @Override
    public List<Team> findAll() {
        return teamRepository.findAll();
    }

    @Override
    public Team findById(int id) {
        Optional<Team> team = teamRepository.findById(id);

        Team theTeam = null;

        if (team.isPresent()) {
            theTeam = team.get();
        } else {
            // Team not found
            throw new ResourceNotFoundException("Did not find team with id: " + id);
        }
        return theTeam;
    }

    @Override
    @Transactional
    public Team save(Team team, int userId) {
        // Ensure creator exists
        User creator = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        // Ensure same team name not being used
        if (teamRepository.existsByNameAndCreatedBy(team.getName(), creator)) {
            throw new DuplicateResourceException("ERROR: You already have a team named: " + team.getName());
        }

        team.setCreatedBy(creator);
        Team savedTeam = teamRepository.save(team);

        // Add creator as OWNER
        TeamMember membership = new TeamMember();
        membership.setTeam(savedTeam);
        membership.setUser(creator);
        membership.setRole(TeamRole.OWNER);
        teamMemberRepository.save(membership);

        return savedTeam;
    }

    // Need to ensure User is validated and team owner
    @Override
    public Team updateTeam(User currentUser, int teamId, UpdateTeamRequest request) {
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new ResourceNotFoundException("Team not found"));

        // Check requesting user is an OWNER of this team
        TeamMember membership = teamMemberRepository
                .findByTeamIdAndUserId(teamId, currentUser.getId())
                .orElseThrow(() -> new AccessDeniedException("Not a member of this team"));

        if (membership.getRole() != TeamRole.OWNER) {
            throw new AccessDeniedException("Only team owners can update the team");
        }

        // update fields only if present
        if(request.getName() != null) team.setName(request.getName());
        if(request.getDescription() != null) team.setDescription(request.getDescription());
        if(request.getIsActive() != null) team.setActive(request.getIsActive());
        if(request.getColor() != null) team.setColor(request.getColor());
        if(request.getIcon() != null) team.setIcon(request.getIcon());

        return teamRepository.save(team);
    }

    @Override
    public void deleteById(int id) {
        // ensure team exists
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found: " + id));

        teamRepository.delete(team);
    }

    //////////////////////////////////////
    /// TEAM MEMBERS ////////////////////
    /////////////////////////////////////

    @Override
    public List<TeamMember> getTeamMembers(
            int teamId,
            User currentUser
    ) {
        teamRepository.findById(teamId).orElseThrow(() -> new ResourceNotFoundException("Team not found"));

        // Must be a team member or owner to view the list of members
        if (!teamMemberRepository.existsByTeamIdAndUserId(teamId, currentUser.getId())) {
            throw new AccessDeniedException("Not a member of this team");
        }

        return teamMemberRepository.findByTeamId(teamId);
    }

    // NOTE ~ On the client side will need a way to handle this to avoid multiple requests
    @Override
    public void addTeamMembers(int teamId, ManageTeamMemberRequest request, User currentUser) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found"));

        // Only OWNER can add members
        TeamMember membership = teamMemberRepository
                .findByTeamIdAndUserId(teamId, currentUser.getId())
                .orElseThrow(() -> new AccessDeniedException("Not a member of this team"));

        if (membership.getRole() != TeamRole.OWNER) {
            throw new AccessDeniedException("Only owners can add members");
        }

        // Add each userId to team
        // Wow this is clean
        for (Integer userId : request.getUserIds()) {
            // skip if already a member (On client side should be disabled or not visible)
            if (teamMemberRepository.existsByTeamIdAndUserId(teamId, userId)) {
                continue;
            }

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            TeamMember newMember = new TeamMember();
            newMember.setTeam(team);
            newMember.setUser(user);
            newMember.setRole(TeamRole.MEMBER);
            teamMemberRepository.save(newMember);
        }
    }

    // remove team members, will use same pattern as passing in User IDs in an array to avoid
    // multiple API calls
    @Transactional
    public void removeTeamMembers(int teamId, ManageTeamMemberRequest request, User currentUser) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found"));

        // Only OWNER can add members
        TeamMember membership = teamMemberRepository
                .findByTeamIdAndUserId(teamId, currentUser.getId())
                .orElseThrow(() -> new AccessDeniedException("Not a member of this team"));

        if (membership.getRole() != TeamRole.OWNER) {
            throw new AccessDeniedException("Only owners can add members");
        }

        // Prevent owner from removing themselves
        if (request.getUserIds().contains(currentUser.getId())) {
            throw new IllegalArgumentException("Owner cannot be removed from the team");
        }

        teamMemberRepository.deleteByTeamIdAndUserIdIn(teamId, request.getUserIds());
    }
}

