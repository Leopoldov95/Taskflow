package com.taskflow.taskflow.dto.team;

import com.taskflow.taskflow.entity.TeamMember;

public class TeamMemberResponse {
    private int id;
    private String firstName;
    private String lastName;
    private String role;

    public TeamMemberResponse(TeamMember member) {
        this.id = member.getUser().getId();
        this.firstName = member.getUser().getFirstName();
        this.lastName = member.getUser().getLastName();
        this.role = member.getRole().name();
    }

    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getRole() { return role; }
}
