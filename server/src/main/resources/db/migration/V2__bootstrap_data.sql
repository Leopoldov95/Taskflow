-- ============================================================
-- Taskflow Bootstrap Seed Data
-- Password for all users: "test"
-- bcrypt hash (cost 10): $2b$10$FMcTiv2/WC316IOmQDro/ugzZLp9KHBpZMyQ3pWW1HN0mFfDV0IwK
-- ============================================================

-- ============================================================
-- USERS (10 total — user 1 is super admin)
-- ============================================================
INSERT INTO `users` (id, first_name, last_name, email, password, created_at, updated_at, is_active) VALUES
(1,  'Admin',  'Super',    'admin@taskflow.io',  '$2b$10$FMcTiv2/WC316IOmQDro/ugzZLp9KHBpZMyQ3pWW1HN0mFfDV0IwK', '2024-01-01 08:00:00', '2024-01-01 08:00:00', TRUE),
(2,  'Alice',  'Nguyen',   'alice@taskflow.io',  '$2b$10$FMcTiv2/WC316IOmQDro/ugzZLp9KHBpZMyQ3pWW1HN0mFfDV0IwK', '2024-01-02 09:00:00', '2024-01-02 09:00:00', TRUE),
(3,  'Bob',    'Carter',   'bob@taskflow.io',    '$2b$10$FMcTiv2/WC316IOmQDro/ugzZLp9KHBpZMyQ3pWW1HN0mFfDV0IwK', '2024-01-02 09:15:00', '2024-01-02 09:15:00', TRUE),
(4,  'Carol',  'Martinez', 'carol@taskflow.io',  '$2b$10$FMcTiv2/WC316IOmQDro/ugzZLp9KHBpZMyQ3pWW1HN0mFfDV0IwK', '2024-01-03 10:00:00', '2024-01-03 10:00:00', TRUE),
(5,  'David',  'Kim',      'david@taskflow.io',  '$2b$10$FMcTiv2/WC316IOmQDro/ugzZLp9KHBpZMyQ3pWW1HN0mFfDV0IwK', '2024-01-03 10:30:00', '2024-01-03 10:30:00', TRUE),
(6,  'Eva',    'Patel',    'eva@taskflow.io',    '$2b$10$FMcTiv2/WC316IOmQDro/ugzZLp9KHBpZMyQ3pWW1HN0mFfDV0IwK', '2024-01-04 11:00:00', '2024-01-04 11:00:00', TRUE),
(7,  'Frank',  'Okafor',   'frank@taskflow.io',  '$2b$10$FMcTiv2/WC316IOmQDro/ugzZLp9KHBpZMyQ3pWW1HN0mFfDV0IwK', '2024-01-04 11:30:00', '2024-01-04 11:30:00', TRUE),
(8,  'Grace',  'Leung',    'grace@taskflow.io',  '$2b$10$FMcTiv2/WC316IOmQDro/ugzZLp9KHBpZMyQ3pWW1HN0mFfDV0IwK', '2024-01-05 12:00:00', '2024-01-05 12:00:00', TRUE),
(9,  'Henry',  'Russo',    'henry@taskflow.io',  '$2b$10$FMcTiv2/WC316IOmQDro/ugzZLp9KHBpZMyQ3pWW1HN0mFfDV0IwK', '2024-01-05 12:30:00', '2024-01-05 12:30:00', TRUE),
(10, 'Isla',   'Thompson', 'isla@taskflow.io',   '$2b$10$FMcTiv2/WC316IOmQDro/ugzZLp9KHBpZMyQ3pWW1HN0mFfDV0IwK', '2024-01-06 13:00:00', '2024-01-06 13:00:00', TRUE);


-- ============================================================
-- USER ROLES
-- User 1 → ROLE_ADMIN (super admin); all others → ROLE_MEMBER
-- ============================================================
INSERT INTO `user_role` (user_id, role_id) VALUES
(1,  2),
(2,  1),
(3,  1),
(4,  1),
(5,  1),
(6,  1),
(7,  1),
(8,  1),
(9,  1),
(10, 1);

-- ============================================================
-- TEAMS (3 teams)
-- Owners: Alice (2) → Product | David (5) → Engineering | Grace (8) → Marketing
-- ============================================================
INSERT INTO `teams` (id, name, description, created_by, is_active, color, icon, created_at, updated_at) VALUES
(1, 'Product',     'Product design and roadmap team.',          2, TRUE, '#6366F1', 'rocket',   '2024-01-10 08:00:00', '2024-01-10 08:00:00'),
(2, 'Engineering', 'Backend, frontend, and infra engineers.',   5, TRUE, '#10B981', 'code',     '2024-01-10 08:30:00', '2024-01-10 08:30:00'),
(3, 'Marketing',   'Growth, content, and brand team.',          8, TRUE, '#F59E0B', 'bullhorn', '2024-01-10 09:00:00', '2024-01-10 09:00:00');

-- ============================================================
-- TEAM MEMBERS
-- Product (1):     Alice(OWNER), Bob, Carol, Henry
-- Engineering (2): David(OWNER), Eva, Frank, Isla
-- Marketing (3):   Grace(OWNER), Carol, Bob, Henry, Isla
-- ============================================================
INSERT INTO `team_member` (team_id, user_id, role, created_at) VALUES
(1, 2,  'OWNER',  '2024-01-10 08:00:00'),
(1, 3,  'MEMBER', '2024-01-10 08:05:00'),
(1, 4,  'MEMBER', '2024-01-10 08:10:00'),
(1, 9,  'MEMBER', '2024-01-10 08:15:00'),
(2, 5,  'OWNER',  '2024-01-10 08:30:00'),
(2, 6,  'MEMBER', '2024-01-10 08:35:00'),
(2, 7,  'MEMBER', '2024-01-10 08:40:00'),
(2, 10, 'MEMBER', '2024-01-10 08:45:00'),
(3, 8,  'OWNER',  '2024-01-10 09:00:00'),
(3, 4,  'MEMBER', '2024-01-10 09:05:00'),
(3, 3,  'MEMBER', '2024-01-10 09:10:00'),
(3, 9,  'MEMBER', '2024-01-10 09:15:00'),
(3, 10, 'MEMBER', '2024-01-10 09:20:00');

-- ============================================================
-- PROJECTS (5 total)
-- Product (1):     Design System, Product Roadmap
-- Engineering (2): API Platform, Infrastructure
-- Marketing (3):   Q1 Campaign
-- ============================================================
INSERT INTO `projects` (id, name, description, team_id, project_key, status, created_at, updated_at) VALUES
(1, 'Design System',   'Company-wide UI component library.',             1, 'DS',   'ACTIVE', '2024-01-15 09:00:00', '2024-01-15 09:00:00'),
(2, 'Product Roadmap', 'Q1-Q2 product planning and milestone tracking.', 1, 'PR',   'ACTIVE', '2024-01-16 09:00:00', '2024-01-16 09:00:00'),
(3, 'API Platform',    'Core REST API and developer platform.',           2, 'API',  'ACTIVE', '2024-01-15 10:00:00', '2024-01-15 10:00:00'),
(4, 'Infrastructure',  'Cloud infra, CI/CD pipelines, and DevOps.',      2, 'INFR', 'ACTIVE', '2024-01-17 10:00:00', '2024-01-17 10:00:00'),
(5, 'Q1 Campaign',     'Q1 go-to-market launch campaign.',               3, 'MKT',  'ACTIVE', '2024-01-18 11:00:00', '2024-01-18 11:00:00');

-- ============================================================
-- TASKS (20 total)
-- Mix of: BACKLOG / IN_PROGRESS / REVIEW / DONE / ARCHIVE
--         LOW / NORMAL / HIGH
-- due_date is always at least 1 day after created_at
-- ============================================================
INSERT INTO `tasks`
  (id, title, description, project_id, team_id, task_key, assignee, status, priority, due_date, created_by, created_at, updated_at)
VALUES
-- ── Design System (project 1, team 1) ─────────────────────────
(1,  'Define color token palette',
     'Establish primary, secondary, and semantic color tokens.',
     1, 1, 1, 3, 'DONE',        'HIGH',   '2024-01-25 17:00:00', 2, '2024-01-17 09:00:00', '2024-01-17 09:00:00'),

(2,  'Build Button component',
     'Create Button with variants: primary, secondary, ghost, danger.',
     1, 1, 2, 4, 'IN_PROGRESS', 'HIGH',   '2024-02-05 17:00:00', 2, '2024-01-18 09:00:00', '2024-01-18 09:00:00'),

(3,  'Build Input component',
     'Text input with label, placeholder, error, and disabled states.',
     1, 1, 3, 3, 'REVIEW',      'NORMAL', '2024-02-08 17:00:00', 2, '2024-01-19 10:00:00', '2024-01-19 10:00:00'),

(4,  'Document Storybook setup',
     'Write Storybook stories and usage docs for all base components.',
     1, 1, 4, 9, 'BACKLOG',     'LOW',    '2024-02-20 17:00:00', 2, '2024-01-20 10:00:00', '2024-01-20 10:00:00'),

-- ── Product Roadmap (project 2, team 1) ───────────────────────
(5,  'Draft Q1 milestone list',
     'Identify key deliverables and owners for Q1.',
     2, 1, 1, 2, 'DONE',        'HIGH',   '2024-01-22 17:00:00', 2, '2024-01-16 09:00:00', '2024-01-16 09:00:00'),

(6,  'Stakeholder review presentation',
     'Prepare slides for exec sign-off on the Q1 plan.',
     2, 1, 2, 9, 'IN_PROGRESS', 'NORMAL', '2024-02-01 17:00:00', 2, '2024-01-23 09:00:00', '2024-01-23 09:00:00'),

(7,  'Competitive analysis update',
     'Review top 5 competitors and update the comparison matrix.',
     2, 1, 3, 4, 'BACKLOG',     'LOW',    '2024-02-15 17:00:00', 9, '2024-01-24 11:00:00', '2024-01-24 11:00:00'),

-- ── API Platform (project 3, team 2) ──────────────────────────
(8,  'Design auth endpoints',
     'Spec out /login, /logout, /refresh, and /me routes.',
     3, 2, 1, 5, 'DONE',        'HIGH',   '2024-01-26 17:00:00', 5, '2024-01-15 10:00:00', '2024-01-15 10:00:00'),

(9,  'Implement JWT middleware',
     'Add token validation middleware to all protected routes.',
     3, 2, 2, 7, 'DONE',        'HIGH',   '2024-01-30 17:00:00', 5, '2024-01-16 10:00:00', '2024-01-16 10:00:00'),

(10, 'Build user CRUD endpoints',
     'GET, POST, PUT, DELETE for the /users resource.',
     3, 2, 3, 6, 'IN_PROGRESS', 'NORMAL', '2024-02-10 17:00:00', 5, '2024-01-20 10:00:00', '2024-01-20 10:00:00'),

(11, 'Write OpenAPI spec',
     'Document all endpoints in OpenAPI 3.0 format.',
     3, 2, 4, 10, 'REVIEW',     'NORMAL', '2024-02-12 17:00:00', 5, '2024-01-22 09:00:00', '2024-01-22 09:00:00'),

(12, 'Rate limiting implementation',
     'Add per-IP and per-user rate limiting using Redis sliding window.',
     3, 2, 5, 7, 'BACKLOG',     'HIGH',   '2024-02-18 17:00:00', 5, '2024-01-25 10:00:00', '2024-01-25 10:00:00'),

(13, 'Standardize API error responses',
     'Define and enforce a consistent error shape across all endpoints.',
     3, 2, 6, 6, 'BACKLOG',     'LOW',    '2024-02-22 17:00:00', 5, '2024-01-26 10:00:00', '2024-01-26 10:00:00'),

-- ── Infrastructure (project 4, team 2) ────────────────────────
(14, 'Set up GitHub Actions CI pipeline',
     'Lint, test, and build jobs triggered on every PR.',
     4, 2, 1, 7, 'DONE',        'HIGH',   '2024-01-28 17:00:00', 5, '2024-01-17 10:00:00', '2024-01-17 10:00:00'),

(15, 'Provision staging environment',
     'Terraform scripts for staging VPC, RDS, and ECS cluster.',
     4, 2, 2, 5, 'IN_PROGRESS', 'HIGH',   '2024-02-06 17:00:00', 5, '2024-01-19 10:00:00', '2024-01-19 10:00:00'),

(16, 'Configure log aggregation',
     'Ship container logs to CloudWatch and set up retention dashboards.',
     4, 2, 3, 10, 'BACKLOG',    'NORMAL', '2024-02-25 17:00:00', 5, '2024-01-28 10:00:00', '2024-01-28 10:00:00'),

(17, 'Secrets management audit',
     'Audit hardcoded credentials and migrate to AWS Secrets Manager.',
     4, 2, 4, 6, 'ARCHIVE',     'HIGH',   '2024-02-03 17:00:00', 5, '2024-01-18 10:00:00', '2024-01-18 10:00:00'),

-- ── Q1 Campaign (project 5, team 3) ───────────────────────────
(18, 'Write launch blog post',
     'Announce the new product with a 600-word blog post.',
     5, 3, 1, 3, 'IN_PROGRESS', 'HIGH',   '2024-02-02 17:00:00', 8, '2024-01-22 11:00:00', '2024-01-22 11:00:00'),

(19, 'Design social media assets',
     'Create banner, square, and story formats for LinkedIn and X.',
     5, 3, 2, 4, 'REVIEW',      'NORMAL', '2024-02-07 17:00:00', 8, '2024-01-23 11:00:00', '2024-01-23 11:00:00'),

(20, 'Schedule email newsletter',
     'Draft and schedule launch announcement to the subscriber list.',
     5, 3, 3, 9, 'BACKLOG',     'LOW',    '2024-02-14 17:00:00', 8, '2024-01-25 11:00:00', '2024-01-25 11:00:00');

-- ============================================================
-- TASK COMMENTS (10 total, spread across tasks)
-- ============================================================
INSERT INTO `task_comments` (task_id, created_by, content, created_at, updated_at) VALUES

(1,  2,  'Color tokens are finalized and exported to Figma. Marking as done.',
     '2024-01-25 14:00:00', '2024-01-25 14:00:00'),

(2,  3,  'Primary and secondary variants are complete. Working on ghost and danger next.',
     '2024-01-20 10:30:00', '2024-01-20 10:30:00'),

(2,  4,  'Let me know when ghost is ready — I can review the accessibility contrast ratios.',
     '2024-01-20 11:00:00', '2024-01-20 11:00:00'),

(3,  3,  'Input component is ready for review. Error and disabled states are both covered.',
     '2024-01-28 15:00:00', '2024-01-28 15:00:00'),

(8,  5,  'Auth endpoint spec reviewed and approved by the team. Moving to implementation.',
     '2024-01-26 16:00:00', '2024-01-26 16:00:00'),

(10, 6,  'GET and POST are done. Starting on PUT and DELETE today.',
     '2024-01-22 09:30:00', '2024-01-22 09:30:00'),

(12, 7,  'Recommending a Redis sliding window algorithm. Happy to own this one.',
     '2024-01-26 11:00:00', '2024-01-26 11:00:00'),

(15, 5,  'Terraform plan looks clean. Will need a manual approval gate before applying to staging.',
     '2024-01-21 13:00:00', '2024-01-21 13:00:00'),

(18, 8,  'First draft is written and shared in Notion. Needs a proofread before we publish.',
     '2024-01-28 10:00:00', '2024-01-28 10:00:00'),

(19, 4,  'LinkedIn banner is done and uploaded. X and story formats still in progress.',
     '2024-01-30 14:30:00', '2024-01-30 14:30:00');