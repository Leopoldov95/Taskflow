package com.taskflow.taskflow.dao;

import com.taskflow.taskflow.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Integer> {
    Optional<TeamMember> findByTeamIdAndUserId(int teamId, int userId);
    boolean existsByTeamIdAndUserId(int teamId, int userId);
    List<TeamMember> findByTeamId(int teamId);
    void deleteByTeamIdAndUserIdIn(int teamId, List<Integer> userIds);
}
