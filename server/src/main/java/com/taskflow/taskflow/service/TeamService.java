package com.taskflow.taskflow.service;

import com.taskflow.taskflow.entity.Team;

import java.util.List;

public interface TeamService {
    List<Team> findAll();
    Team findById(int id);
    Team save(Team team);
    Team updateTeam(Team team);
    void deleteById(int id);
}
