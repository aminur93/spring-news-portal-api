-- V2__create_menu_table.sql

-- Create menus table
CREATE TABLE menus (
   id INT AUTO_INCREMENT PRIMARY KEY,
   permission_id INT NULL,
   parent_id INT NULL,
   name_en VARCHAR(125) NOT NULL,
   name_bn VARCHAR(125) NOT NULL,
   url VARCHAR(55) NULL,
   icon VARCHAR(55) NULL,
   header_menu TINYINT(4) NULL,
   sidebar_menu TINYINT(4) null,
   dropdown_menu TINYINT(4) null,
   children_parent_menu TINYINT(4) null,
   status TINYINT(4) null,
   created_by INT null,
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

INSERT INTO menus (permission_id, parent_id, name_en, name_bn, icon, url, header_menu, sidebar_menu, dropdown_menu, children_parent_menu, status) VALUES
(1, NULL, 'Dashboard', 'ড্যাশবোর্ড', 'mdi mdi-view-dashboard', '/dashboard', false, true, false, NULL, 1),
(NULL, NULL, 'User Management', 'ব্যবহারকারী ব্যবস্থাপনা', 'mdi mdi-account-group', NULL, false, true, false, NULL, 1),
(NULL, NULL, 'Settings', 'সেটিংস', 'mdi mdi-account-group', NULL, false, true, false, NULL, 1),

-- User Management User Start
(10, 2, 'User', 'উজার', 'mdi mdi-plus', '/user', false, true, true, true, 1),
(11, 2, 'Add User', 'উজার যোগ', 'mdi mdi-plus', NULL, false, true, true, false, 1),
(12, 2, 'Edit User', 'উজার এডিট', 'mdi mdi-plus', NULL, false, true, true, false, 1),
(13, 2, 'Delete User', 'উজার ডিলিট', 'mdi mdi-plus', NULL, false, true, true, false, 1),
-- User Management User End

-- User Management Role Start
(6, 2, 'Role', 'রোল', 'mdi mdi-plus', '/role', false, true, true, true, 1),
(7, 2, 'Add Role', 'রোল যোগ', 'mdi mdi-plus', NULL, false, true, true, false, 1),
(8, 2, 'Edit Role', 'রোল এডিট', 'mdi mdi-plus', NULL, false, true, true, false, 1),
(9, 2, 'Delete Role', 'রোল ডিলিট', 'mdi mdi-plus', NULL, false, true, true, false, 1),
-- User Management Role End

-- User Management Permission Start
(2, 2, 'Permission', 'পারমিশন', 'mdi mdi-plus', '/permission', false, true, true, true, 1),
(3, 2, 'Add Permission', 'পারমিশন যোগ', 'mdi mdi-plus', NULL, false, true, true, false, 1),
(4, 2, 'Edit Permission', 'পারমিশন এডিট', 'mdi mdi-plus', NULL, false, true, true, false, 1),
(5, 2, 'Delete Permission', 'পারমিশন ডিলিট', 'mdi mdi-plus', NULL, false, true, true, false, 1),
-- User Management Permission End

-- Settings Menu Start
(14, 3, 'Menu', 'মেনু', 'mdi mdi-plus', '/menu', false, true, true, true, 1),
(15, 3, 'Add Menu', 'মেনু যোগ', 'mdi mdi-plus', NULL, false, true, true, false, 1),
(16, 3, 'Edit Menu', 'মেনু এডিট', 'mdi mdi-plus', NULL, false, true, true, false, 1),
(17, 3, 'Delete Menu', 'মেনু ডিলিট', 'mdi mdi-plus', NULL, false, true, true, false, 1)
-- Settings Menu End;
