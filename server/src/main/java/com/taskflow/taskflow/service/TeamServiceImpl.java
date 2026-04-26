package com.taskflow.taskflow.service;

import com.taskflow.taskflow.dao.TeamRepository;
import com.taskflow.taskflow.dao.UserRepository;
import com.taskflow.taskflow.entity.Team;
import com.taskflow.taskflow.entity.User;
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

    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository, UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
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
    public Team save(Team team) {
        // Ensure creator exists
        int creatorId = team.getCreatedBy().getId();

        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("User not found: " + creatorId));

        team.setCreatedBy(creator);

        // Optionally auto-add creator as member
        team.setMembers(Set.of(creator));

        return teamRepository.save(team);
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
