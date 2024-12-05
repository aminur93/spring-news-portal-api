-- V1__create_acl_all_table.sql

-- Create users table
CREATE TABLE users (
   id INT AUTO_INCREMENT PRIMARY KEY,
   name_en VARCHAR(255) NOT NULL,
   name_bn VARCHAR(255) NOT NULL,
   email VARCHAR(255) UNIQUE NOT NULL,
   phone_en VARCHAR(11) UNIQUE NOT NULL,
   phone_bn VARCHAR(11) UNIQUE NOT NULL,
   email_verified_at TIMESTAMP NULL,
   password VARCHAR(255) NOT NULL,
   remember_token VARCHAR(100) NULL,
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create roles table
CREATE TABLE roles (
   id INT AUTO_INCREMENT PRIMARY KEY,
   name_en VARCHAR(255) UNIQUE NOT NULL,
   name_bn VARCHAR(255) UNIQUE NOT NULL,
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create permissions table
CREATE TABLE permissions (
     id INT AUTO_INCREMENT PRIMARY KEY,
     title_en VARCHAR(255) NOT NULL,
     title_bn VARCHAR(255) NOT NULL,
     name_en VARCHAR(255) UNIQUE NOT NULL,
     name_bn VARCHAR(255) UNIQUE NOT NULL,
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create role_has_permission join table
CREATE TABLE role_has_permission (
     role_id INT NOT NULL,
     permission_id INT NOT NULL,
     PRIMARY KEY (role_id, permission_id),
     FOREIGN KEY (role_id) REFERENCES roles(id),
     FOREIGN KEY (permission_id) REFERENCES permissions(id)
);

-- Create model_has_role join table
CREATE TABLE model_has_role (
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);


-- Insert roles
INSERT INTO roles (name_en, name_bn) VALUES
('SuperAdmin', 'সুপারঅ্যাডমিন'),
('Admin', 'অ্যাডমিন'),
('Editor', 'সম্পাদক'),
('Reporter', 'সংবাদদাতা'),
('Agent', 'এজেন্ট');

-- Insert permissions
INSERT INTO permissions (title_en, title_bn, name_en, name_bn) VALUES
('Dashboard', 'ড্যাশবোর্ড', 'dashboard', 'ড্যাশবোর্ড'),
('Permission', 'অনুমতি', 'permission-list', 'অনুমতি-তালিকা'),
('Permission', 'অনুমতি', 'permission-create', 'অনুমতি-তৈরি'),
('Permission', 'অনুমতি', 'permission-edit', 'অনুমতি-সম্পাদনা'),
('Permission', 'অনুমতি', 'permission-delete', 'অনুমতি-মুছে ফেলুন'),
('Role', 'ভূমিকা', 'role-list', 'ভূমিকা-তালিকা'),
('Role', 'ভূমিকা', 'role-create', 'ভূমিকা-তৈরি'),
('Role', 'ভূমিকা', 'role-edit', 'ভূমিকা-সম্পাদনা'),
('Role', 'ভূমিকা', 'role-delete', 'ভূমিকা-মুছে ফেলুন'),
('User', 'ব্যবহারকারী', 'user-list', 'ব্যবহারকারী-তালিকা'),
('User', 'ব্যবহারকারী', 'user-create', 'ব্যবহারকারী-তৈরি'),
('User', 'ব্যবহারকারী', 'user-edit', 'ব্যবহারকারী-সম্পাদনা'),
('User', 'ব্যবহারকারী', 'user-delete', 'ব্যবহারকারী-মুছে ফেলুন'),
('Menu', 'মেনু', 'menu-list', 'মেনু-তালিকা'),
('Menu', 'মেনু', 'menu-create', 'মেনু-তৈরি'),
('Menu', 'মেনু', 'menu-edit', 'মেনু-সম্পাদনা'),
('Menu', 'মেনু', 'menu-delete', 'মেনু-মুছে ফেলুন');


-- Insert users
INSERT INTO users (name_en, name_bn, email, phone_en, phone_bn, password) VALUES
('super-admin','সুপার অ্যাডমিন', 'superadmin@gmail.com', '12345678901','12345678901', '$2a$10$GJJsH9YA/zw8tuWEPw6xb.qns6BPk14N6tkNrb6kZ8bEGVTXjppS6'), -- password: password
('admin', 'অ্যাডমিন', 'admin@gmail.com', '12345678902', '12345678902', '$2a$10$GJJsH9YA/zw8tuWEPw6xb.qns6BPk14N6tkNrb6kZ8bEGVTXjppS6'),       -- password: password
('editor', 'সম্পাদক', 'editor@gmail.com', '12345678903', '12345678903', '$2a$10$GJJsH9YA/zw8tuWEPw6xb.qns6BPk14N6tkNrb6kZ8bEGVTXjppS6'),      -- password: password
('reporter', 'রিপোর্টার', 'reporter@gmail.com', '12345678904', '12345678904', '$2a$10$GJJsH9YA/zw8tuWEPw6xb.qns6BPk14N6tkNrb6kZ8bEGVTXjppS6'),      -- password: password
('agent', 'এজেন্ট', 'agent@gmail.com', '12345678905', '12345678905', '$2a$10$GJJsH9YA/zw8tuWEPw6xb.qns6BPk14N6tkNrb6kZ8bEGVTXjppS6');        -- password: password

-- Assign roles to users
INSERT INTO model_has_role (user_id, role_id) VALUES
(1, 1), -- super-admin -> SuperAdmin
(2, 2), -- admin -> Admin
(3, 3), -- editor -> Editor
(4, 4), -- reporter -> Reporter
(5, 5); -- agent -> Agent

-- Assign permissions to roles (SuperAdmin gets all permissions)
INSERT INTO role_has_permission (role_id, permission_id)
SELECT 1, id FROM permissions; -- SuperAdmin has all permissions

-- Example of Admin having specific permissions
INSERT INTO role_has_permission (role_id, permission_id) VALUES
(2, 5), -- Admin -> role-list
(2, 6), -- Admin -> role-create
(2, 7), -- Admin -> role-edit
(2, 8), -- Admin -> role-delete
(2, 9), -- Admin -> user-list
(2, 10), -- Admin -> user-create
(2, 11), -- Admin -> user-edit
(2, 12); -- Admin -> user-delete

-- Example of Editor having specific permissions
INSERT INTO role_has_permission (role_id, permission_id) VALUES
(3, 1), -- Editor -> permission-list
(3, 3), -- Editor -> permission-edit
(3, 9), -- Editor -> user-list
(3, 11); -- Editor -> user-edit

-- Example of Editor having specific permissions
INSERT INTO role_has_permission (role_id, permission_id) VALUES
(4, 1), -- Editor -> permission-list
(4, 3), -- Editor -> permission-edit
(4, 9), -- Editor -> user-list
(4, 11); -- Editor -> user-edit

-- Example of Agent having specific permissions
INSERT INTO role_has_permission (role_id, permission_id) VALUES
(5, 9), -- Agent -> user-list
(5, 11); -- Agent -> user-edit
