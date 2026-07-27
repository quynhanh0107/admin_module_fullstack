-- 1. Tạo 3 Vai trò cốt lõi
INSERT INTO roles (name) VALUES ('ADMIN'), ('TEACHER'), ('STUDENT') ON CONFLICT DO NOTHING;

-- 2. Tạo các Quyền (Actions) theo nghiệp vụ Học vụ
INSERT INTO actions (code, module) VALUES 
('CREATE_CLASS', 'COURSE_MANAGEMENT'),
('ASSIGN_TEACHERS', 'COURSE_MANAGEMENT'),
('INPUT_GRADES', 'ACADEMIC'),
('UPDATE_COURSE_STATUS', 'ACADEMIC'),
('VIEW_COURSE_MEMBERS', 'ACADEMIC'),
('VIEW_OWN_TRANSCRIPT', 'STUDENT_PORTAL'),
('REGISTER_DEGREE', 'STUDENT_PORTAL')
ON CONFLICT DO NOTHING;

-- 3. Gán Quyền cho ADMIN (Được tạo lớp và phân công Giáo viên)
INSERT INTO role_actions (role_id, action_id)
SELECT r.id, a.id FROM roles r, actions a 
WHERE r.name = 'ADMIN' AND a.code IN ('CREATE_CLASS', 'ASSIGN_TEACHERS')
ON CONFLICT DO NOTHING;

-- 4. Gán Quyền cho TEACHER (Được nhập điểm và cập nhật trạng thái)
INSERT INTO role_actions (role_id, action_id)
SELECT r.id, a.id FROM roles r, actions a 
WHERE r.name = 'TEACHER' AND a.code IN ('INPUT_GRADES', 'UPDATE_COURSE_STATUS')
ON CONFLICT DO NOTHING;

-- 5. PHONG ẤN QUYỀN LỰC: Gán tài khoản hiện tại của bạn thành ADMIN
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r 
WHERE u.username = 'quynhanhle' AND r.name = 'ADMIN'
ON CONFLICT DO NOTHING;