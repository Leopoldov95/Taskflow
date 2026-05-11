-- Taskflow DB Commands

-- Initial Cleanup (For Testing)
CREATE DATABASE  IF NOT EXISTS `taskflow`;
USE `taskflow`;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `users`;
DROP TABLE IF EXISTS `roles`;
DROP TABLE IF EXISTS `user_role`;
DROP TABLE IF EXISTS `teams`;
DROP TABLE IF EXISTS `team_member`;
DROP TABLE IF EXISTS `tasks`;
DROP TABLE IF EXISTS `task_comment`;
DROP TABLE IF EXISTS `documents`;

SET FOREIGN_KEY_CHECKS = 1;

-- Create User table

CREATE TABLE `users` (
	id INT NOT NULL AUTO_INCREMENT,
	first_name VARCHAR(50) DEFAULT NULL,
	last_name VARCHAR(50) DEFAULT NULL,
	email VARCHAR(50) UNIQUE NOT NULL,
	password VARCHAR(255) NOT NULL,
	created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	is_active BOOLEAN NOT NULL DEFAULT TRUE,
	PRIMARY KEY (id)
)ENGINE=InnoDB;

-- ALTER TABLE `users` 
-- ADD password VARCHAR(255) NOT NULL;

-- Create Role table
CREATE TABLE `roles` (
	id INT NOT NULL AUTO_INCREMENT,
	role VARCHAR(50) NOT NULL UNIQUE,
	PRIMARY KEY (id)
);

-- Create User_Role table
CREATE TABLE `user_role` (
	id INT NOT NULL AUTO_INCREMENT,
	user_id INT NOT NULL,
	role_id INT NOT NULL,
	PRIMARY KEY (id),
	INDEX (user_id),
	INDEX (role_id)
);

-- Create Team table
CREATE TABLE `teams` (
	id INT NOT NULL AUTO_INCREMENT,
	created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	name VARCHAR(100) DEFAULT NULL UNIQUE,
	description TEXT,
	created_by INT NOT NULL,
	is_active BOOLEAN DEFAULT TRUE,
	color VARCHAR(20),
	icon VARCHAR(20),
	PRIMARY KEY (id),
	INDEX(created_by),
	CONSTRAINT fk_created_by_user FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE RESTRICT
);

-- Create Team member table
-- TODO: In the future, may want a way to handle roles here instead
CREATE TABLE `team_member` (
	id INT NOT NULL AUTO_INCREMENT,
	created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	team_id INT NOT NULL,
	user_id INT NOT NULL,
	role ENUM('OWNER', 'MEMBER') NOT NULL DEFAULT 'MEMBER',
	PRIMARY KEY (id),
	UNIQUE (team_id, user_id),
	INDEX (team_id),
	INDEX (user_id),
	CONSTRAINT fk_tm_team_id FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE RESTRICT ON UPDATE CASCADE,
	CONSTRAINT fk_tm_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- Create Project table

CREATE TABLE `projects` (
	id INT NOT NULL AUTO_INCREMENT,
	created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	name VARCHAR(50) NOT NULL,
	description TEXT,
	team_id INT NOT NULL,
	project_key VARCHAR(4) NOT NULL,
	status ENUM ('ACTIVE', 'ARCHIVE', 'COMPLETED') NOT NULL DEFAULT 'ACTIVE',
	PRIMARY KEY (id),
	CONSTRAINT fk_team_id FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE RESTRICT,
	INDEX (team_id),
	UNIQUE (team_id, project_key),
    UNIQUE (team_id, name)
);

-- Create Task table

CREATE TABLE `tasks` (
	id INT NOT NULL AUTO_INCREMENT,
	created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	name VARCHAR(100) NOT NULL,
	description TEXT,
	project_id INT NOT NULL,
	team_id INT NOT NULL,
	task_key INT NOT NULL,
	assignee INT,
	status ENUM ('BACKLOG', 'IN_PROGRESS', 'REVIEW', 'DONE', 'ARCHIVE') DEFAULT 'BACKLOG',
	due_date TIMESTAMP NULL,
	priority ENUM ('LOW', 'NORMAL', 'HIGH') DEFAULT 'NORMAL',
	created_by INT NOT NULL,
	deleted_at TIMESTAMP DEFAULT NULL,
	PRIMARY KEY (id),
	UNIQUE (project_id, task_key),
	INDEX (project_id),
	INDEX (team_id),
	INDEX (assignee),
	INDEX (status),
	CONSTRAINT fk_task_project_id FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE RESTRICT,
	CONSTRAINT fk_task_team_id FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE RESTRICT,
	CONSTRAINT fk_task_assignee_id FOREIGN KEY (assignee) REFERENCES users(id) ON DELETE RESTRICT,
	CONSTRAINT fk_task_creator_id FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE RESTRICT
);

-- Create Task Comment table
CREATE TABLE `task_comments` (
	id INT NOT NULL AUTO_INCREMENT,
	created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	task_id INT NOT NULL,
	created_by INT,
	content TEXT NOT NULL,
	deleted_at DATETIME NULL,
	PRIMARY KEY (id),
	INDEX (task_id),
	INDEX (created_by),
	INDEX (task_id, created_at),
	CONSTRAINT fk_comment_task_id FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE,
	CONSTRAINT fk_comment_user_id FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL
);

-- Create Document table

CREATE TABLE `documents` (
	id INT NOT NULL AUTO_INCREMENT,
	created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	created_by INT NOT NULL,
	title VARCHAR(100) NOT NULL,
	last_modified_by INT,
	content TEXT NOT NULL,
	deleted_at DATETIME NULL,
	task_id INT NULL,
    comment_id INT NULL,
	PRIMARY KEY (id),
	INDEX (created_by),
	INDEX (last_modified_by),
	INDEX (task_id),
	INDEX (comment_id),
	CONSTRAINT fk_document_creator_id FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE RESTRICT,
	CONSTRAINT fk_document_modified_id FOREIGN KEY (last_modified_by) REFERENCES users(id) ON DELETE SET NULL,
	CONSTRAINT fk_document_task_id FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE,
    CONSTRAINT fk_document_comment_id FOREIGN KEY (comment_id) REFERENCES task_comments(id) ON DELETE CASCADE,
    CONSTRAINT check_document_owner CHECK (
    	(task_id IS NOT NULL AND comment_id IS NULL) OR
    	(task_id IS NULL AND comment_id IS NOT NULL)
	)
);

-- Define additional FOREIGN KEYS and CONSTRAINST

ALTER TABLE `users` ADD
CONSTRAINT check_email CHECK (email LIKE '%_@_%.__%');

ALTER TABLE `user_role` ADD
CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE `user_role` ADD
CONSTRAINT fk_role_id FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE RESTRICT ON UPDATE CASCADE;

-- Add Indexes where needed

ALTER TABLE `team_member` ADD (
INDEX `team_id_index` (`team_id`),
INDEX `user_id_index` (`user_id`)
);


-- --------------------
-- ROLES
-- --------------------
INSERT INTO roles (id, role) VALUES
(1, 'ROLE_MEMBER'),
(2, 'ROLE_ADMIN');