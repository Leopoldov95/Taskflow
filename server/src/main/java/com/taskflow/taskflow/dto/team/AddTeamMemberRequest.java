package com.taskflow.taskflow.dto.team;

import java.util.List;

/**
 * DTO for adding new team members
 * We want to accept a list of User IDs to avoid situations where adding multiple users
 * Results in making multiple API calls
 */
public class AddTeamMemberRequest {
    public List<Integer> userIds;

    public List<Integer> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Integer> userIds) {
        this.userIds = userIds;
    }
}
