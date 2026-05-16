package com.taskflow.taskflow.dao;

import com.taskflow.taskflow.entity.Team;
import com.taskflow.taskflow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Integer> {

    // Add any custom Query logic here

    // Checks if duplicate team name exists for existing user
    boolean existsByNameAndCreatedBy(String name, User createdBy);
}
