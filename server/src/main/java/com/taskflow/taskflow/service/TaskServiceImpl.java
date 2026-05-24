package com.taskflow.taskflow.service;

import com.taskflow.taskflow.dao.*;
import com.taskflow.taskflow.dto.task.CreateTaskRequest;
import com.taskflow.taskflow.dto.task.UpdateTaskRequest;
import com.taskflow.taskflow.dto.taskcomment.CreateTaskCommentRequest;
import com.taskflow.taskflow.dto.taskcomment.UpdateTaskCommentRequest;
import com.taskflow.taskflow.entity.Project;
import com.taskflow.taskflow.entity.Task;
import com.taskflow.taskflow.entity.TaskComment;
import com.taskflow.taskflow.entity.User;
import com.taskflow.taskflow.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import com.taskflow.taskflow.dto.task.TaskResponse;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskCommentRepository taskCommentRepository;
    private TaskRepository taskRepository;
    private ProjectRepository projectRepository;
    private TeamRepository teamRepository;
    private TeamMemberRepository teamMemberRepository;
    private TeamAccessService teamAccessService;
    private AuthService authService;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository,
                           ProjectRepository projectRepository,
                           TeamRepository teamRepository,
                           TeamMemberRepository teamMemberRepository,
                           TeamAccessService teamAccessService,
                           AuthService authService,
                           TaskCommentRepository taskCommentRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.teamRepository = teamRepository;
        this.teamMemberRepository = teamMemberRepository;
        this.teamAccessService = teamAccessService;
        this.authService = authService;
        this.taskCommentRepository = taskCommentRepository;
    }

    @Override
    public Page<TaskResponse> findAllByProjectId(int projectId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        // will need to ensure User is part of the project
        User currentUser = authService.getCurrentUser();

        //1. Retrieve Project
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project with id not found: " + projectId));

        //2. Check that user is a member of the TEAM
        teamAccessService.validateTeamAccess(project.getTeam().getId(), currentUser.getId());

        // get all tasks for that project
        Page<Task> tasks = taskRepository.findAllByProjectId(projectId, pageable);

        return tasks.map(TaskResponse::new);
    }

    @Override
    public Task findById(int taskId) {
        // will need to ensure User has access
        User currentUser = authService.getCurrentUser();

        //1. Retrieve Task
        Task task = taskRepository.findById(taskId)
        .orElseThrow(() -> new ResourceNotFoundException("Task with id not found: " + taskId));

        //2. Check that user is a member of the TEAM
        teamAccessService.validateTeamAccess(task.getTeam().getId(), currentUser.getId());

        // get task
        return task;
    }

    @Transactional
    @Override
    public Task save(int projectId, CreateTaskRequest request) {
        // will need to ensure User has access
        User currentUser = authService.getCurrentUser();
        // Check that user is a member of the TEAM
        //1. Retrieve Project
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project with id not found: " + projectId));

        //2. Check that user is a member of the TEAM
        teamAccessService.validateTeamAccess(project.getTeam().getId(), currentUser.getId());

        // User is valid and authorized, create new task
        Task newTask = new Task();
        newTask.setTitle(request.getTitle());
        newTask.setDescription(request.getDescription());

        // set values based on app data
        int nextTaskKey = taskRepository.findMaxTaskKeyByProjectId(projectId) + 1;
        newTask.setTaskKey(nextTaskKey);

        newTask.setCreatedBy(currentUser);
        newTask.setProject(project);
        newTask.setTeam(project.getTeam());

        // Optional fields
        if (request.getDueDate() != null) {
            if (request.getDueDate().isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException(
                        "Due date cannot be in the past"
                );
            }
            newTask.setDueDate(request.getDueDate());
        }

        if (request.getPriority() != null) {
            newTask.setPriority(request.getPriority());
        }

        // no need to handle status, by default goes to BACKLOG

        return taskRepository.save(newTask);
    }

    @Override
    public Task update(int taskId, UpdateTaskRequest request) {
        User currentUser = authService.getCurrentUser();
        // check Task exists
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id not found: " + taskId));

        // ensure user is a member of the Team owning the task
        teamAccessService.validateTeamAccess(task.getTeam().getId(), currentUser.getId());

        // set the new Task updates
        // ensure due date cannot be before createdAt date
        if (request.getDueDate() != null) {
            if (request.getDueDate().isBefore(task.getCreatedAt())) {
                throw new IllegalArgumentException(
                        "Due date cannot be in the past"
                );
            }
            task.setDueDate(request.getDueDate());
        }
        if (request.getPriority() != null) task.setPriority(request.getPriority());
        if (request.getStatus() != null) task.setStatus(request.getStatus());
        if (request.getTitle() != null) task.setTitle(request.getTitle());
        if (request.getDescription() != null) task.setDescription(request.getDescription());

        return taskRepository.save(task);
    }

    @Override
    public void deleteById(int taskId) {
        User currentUser = authService.getCurrentUser();
        // check Task exists
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id not found: " + taskId));

        // ensure user is a member of the Team owning the task
        teamAccessService.validateTeamAccess(task.getTeam().getId(), currentUser.getId());

        // remove form BD
        taskRepository.delete(task);
    }

    //------------------------
    // TASK COMMENTS
    //----------------------

    @Override
    public TaskComment saveComment(int taskId, CreateTaskCommentRequest request) {
        User currentUser = authService.getCurrentUser();
        // check Task exists
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id not found: " + taskId));

        // ensure user is a member of the Team owning the task
        teamAccessService.validateTeamAccess(task.getTeam().getId(), currentUser.getId());

        // create the new task comment
        TaskComment taskComment = new TaskComment();
        taskComment.setContent(request.getContent());
        taskComment.setCreatedBy(currentUser);
        taskComment.setTask(task);

        return taskCommentRepository.save(taskComment);
    }

    @Override
    public TaskComment updateComment(int taskCommentId, UpdateTaskCommentRequest request) {
        User currentUser = authService.getCurrentUser();
        // check Task exists
        TaskComment taskComment = taskCommentRepository.findById(taskCommentId)
                .orElseThrow(() -> new ResourceNotFoundException("Task Comment with id not found: " + taskCommentId));

        // ensure user is comment owner
        if (taskComment.getCreatedBy().getId() != currentUser.getId()) {
            throw new AccessDeniedException("Must be comment owner to update task comment");
        }

        // update fields
        taskComment.setContent(request.getContent());

        return taskCommentRepository.save(taskComment);
    }

    @Override
    public void deleteComment(int taskCommentId) {
        User currentUser = authService.getCurrentUser();
        // check Task exists
        TaskComment taskComment = taskCommentRepository.findById(taskCommentId)
                .orElseThrow(() -> new ResourceNotFoundException("Task Comment with id not found: " + taskCommentId));

        // ensure user is comment owner
        if (taskComment.getCreatedBy().getId() != currentUser.getId()) {
            throw new AccessDeniedException("Must be comment owner to delete task comment");
        }
        // remove from DB
        taskCommentRepository.delete(taskComment);
    }
}
