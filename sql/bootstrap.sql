-- --------------------
-- BOOTSTRAP DATA
-- --------------------


USE taskflow;

-- --------------------
-- ROLES
-- --------------------
INSERT INTO roles (id, role) VALUES
(1, 'ROLE_MEMBER'),
(2, 'ROLE_ADMIN');

-- --------------------
-- USERS
-- --------------------
INSERT INTO users (id, first_name, last_name, email, password, role_id) VALUES
(1, 'John', 'Doe', 'john.doe@test.com', '{bcrypt}$2a$10$md.GKoDHFet7Ek3XCdr7ae.CaBN/Cl40vau3v5kSbeDVs6PMGHLR2', 2),
(2, 'Jane', 'Smith', 'jane.smith@test.com', '{bcrypt}$2a$10$md.GKoDHFet7Ek3XCdr7ae.CaBN/Cl40vau3v5kSbeDVs6PMGHLR2', 1),
(3, 'Bob', 'Brown', 'bob.brown@test.com', '{bcrypt}$2a$10$md.GKoDHFet7Ek3XCdr7ae.CaBN/Cl40vau3v5kSbeDVs6PMGHLR2', 1);

-- --------------------
-- USER ROLE (mapping table)
-- --------------------
INSERT INTO user_role (id, user_id, role_id) VALUES
(1, 1, 2),
(2, 2, 1),
(3, 3, 1);

-- --------------------
-- TEAMS
-- --------------------
INSERT INTO teams (id, name, description, created_by, color, icon) VALUES
(1, 'Core Team', 'Main development team', 1, 'blue', 'users'),
(2, 'QA Team', 'Testing and QA team', 1, 'green', 'check');

-- --------------------
-- TEAM MEMBERS
-- --------------------
INSERT INTO team_member (id, team_id, user_id) VALUES
(1, 1, 1),
(2, 1, 2),
(3, 2, 1),
(4, 2, 3);

-- --------------------
-- PROJECTS
-- --------------------
INSERT INTO projects (id, name, description, team_id, project_key, status) VALUES
(1, 'Taskflow API', 'Backend service', 1, 'TA', 'ACTIVE'),
(2, 'Frontend UI', 'React frontend', 1, 'FE', 'ACTIVE'),
(3, 'Automation', 'QA automation suite', 2, 'QA', 'ACTIVE');

-- --------------------
-- TASKS
-- --------------------
INSERT INTO tasks (id, name, description, project_id, task_key, assignee, status, priority, created_by) VALUES
(1, 'Setup project', 'Initialize Spring Boot project', 1, 1, 1, 'DONE', 'HIGH', 1),
(2, 'Create auth module', 'Implement JWT auth', 1, 2, 2, 'IN_PROGRESS', 'HIGH', 1),
(3, 'Design UI', 'Create initial UI mockups', 2, 1, 2, 'BACKLOG', 'NORMAL', 1),
(4, 'Write test cases', 'Prepare automation tests', 3, 1, 3, 'BACKLOG', 'LOW', 1);

-- --------------------
-- TASK COMMENTS
-- --------------------
INSERT INTO task_comments (id, task_id, created_by, content) VALUES
(1, 1, 2, 'Great job on setup!'),
(2, 2, 1, 'Auth module needs review'),
(3, 3, 2, 'Working on wireframes');

-- --------------------
-- DOCUMENTS
-- --------------------
INSERT INTO documents (id, created_by, title, last_modified_by, content, task_id, comment_id) VALUES
-- Linked to task
(1, 1, 'Setup Guide', 1, 'Project setup documentation', 1, NULL),
(2, 2, 'Auth Design', 2, 'Authentication flow design', 2, NULL),
-- Linked to comment
(3, 2, 'Comment Attachment', 2, 'Extra notes for comment', NULL, 1);
