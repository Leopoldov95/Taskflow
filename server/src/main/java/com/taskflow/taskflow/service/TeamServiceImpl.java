package com.taskflow.taskflow.service;

import com.taskflow.taskflow.dao.TeamMemberRepository;
import com.taskflow.taskflow.dao.TeamRepository;
import com.taskflow.taskflow.dao.UserRepository;
import com.taskflow.taskflow.dto.team.CreateTeamRequest;
import com.taskflow.taskflow.entity.Team;
import com.taskflow.taskflow.entity.TeamMember;
import com.taskflow.taskflow.entity.User;
import com.taskflow.taskflow.entity.enums.TeamRole;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
            // User not found
            throw new RuntimeException("Did not find team with id: " + id);
        }
        return theTeam;
    }

    @Override
    @Transactional
    public Team save(Team team, int userId) {
        // Ensure creator exists
        User creator = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

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

    @Override
    public Team updateTeam(Team team) {
        return teamRepository.save(team);
    }

    @Override
    public void deleteById(int id) {
        teamRepository.deleteById(id);
    }
}
