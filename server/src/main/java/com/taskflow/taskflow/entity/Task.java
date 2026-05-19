package com.taskflow.taskflow.entity;

import com.taskflow.taskflow.entity.enums.RoleType;
import com.taskflow.taskflow.entity.enums.TaskPriority;
import com.taskflow.taskflow.entity.enums.TaskStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "tasks")
public class Task {
    // Define fields

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="description", nullable = false)
    private String description;

    @Column(name = "created_at")
    @CreationTimestamp
    private Date createdAt;

    @Column(name="updated_at")
    @UpdateTimestamp
    private Date updatedAt;

    @Column(name="task_key", nullable = false)
    private int taskKey;

    @Column(name = "status", nullable = false)
    private TaskStatus status;

    @Column(name = "priority", nullable = false)
    private TaskPriority priority;

    @Column(name = "due_date")
    private Date dueDate;

    @Column(name = "deleted_at")
    private Date deletedAt;

    // Foreign Keys & Join Tables

    // Many Tasks -> One Projects
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="project_id", nullable = false)
    private Project project;

    // Many Tasks -> One User
    @ManyToOne
    @JoinColumn(name = "assignee")
    private User assignee;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    // Many tasks -> One Team
    @ManyToOne
    @JoinColumn(name = "tasks")
    private Team team;
}
