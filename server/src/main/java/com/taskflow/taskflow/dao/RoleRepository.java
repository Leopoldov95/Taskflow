package com.taskflow.taskflow.dao;

import com.taskflow.taskflow.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    // Allows us to find a role based on the ENUM name (ROLE_ADMIN, ROLE_MEMBER)
    //
    Role findByName(String name);
}