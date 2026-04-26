package com.taskflow.taskflow.dao;

import com.taskflow.taskflow.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Integer> {

    // Add any custom Query logic here
}
