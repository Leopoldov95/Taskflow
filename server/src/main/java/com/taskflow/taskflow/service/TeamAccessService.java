package com.taskflow.taskflow.service;

import com.taskflow.taskflow.dao.TeamMemberRepository;
import com.taskflow.taskflow.dao.TeamRepository;
import com.taskflow.taskflow.entity.TeamMember;
import com.taskflow.taskflow.entity.enums.TeamRole;
import com.taskflow.taskflow.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

/**
 * Helper Service class to determine if User is a MEMBER/OWNER of a Team
 * Necessary to determine if they can access nested Team resources
 * Projects, Tasks, etc
 */

@Service
public class TeamAccessService {

    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    @Autowired
    public TeamAccessService(TeamRepository teamRepository, TeamMemberRepository teamMemberRepository) {
        this.teamRepository = teamRepository;
        this.teamMemberRepository = teamMemberRepository;
    }

    /**
     * Validates that the team exists and the user is a member of it.
     * @return TeamMember — useful for further role checks in the calling service
     */
    public TeamMember validateTeamAccess(int teamId, int userId) {
        teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found: " + teamId));

        return teamMemberRepository.findByTeamIdAndUserId(teamId, userId)
                .orElseThrow(() -> new AccessDeniedException("Not a member of this team"));
    }

    /**
     * Validates that the team exists and the user is an OWNER of it.
     */
    public void validateOwnerAccess(int teamId, int userId) {
        TeamMember membership = validateTeamAccess(teamId, userId);

        if (membership.getRole() != TeamRole.OWNER) {
            throw new AccessDeniedException("Only owners can perform this action");
        }
    }

    /**
     * Simple boolean check — useful when we want to conditionally
     * show/hide data rather than throw an exception.
     */
    public boolean isTeamMember(int teamId, int userId) {
        teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found: " + teamId));

        return teamMemberRepository.existsByTeamIdAndUserId(teamId, userId);
    }

}
