-- 1. Bảng Hồ sơ người dùng (Liên kết 1-1 với bảng users)
CREATE TABLE IF NOT EXISTS user_profiles (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    full_name VARCHAR(100) NOT NULL,
    identity_number VARCHAR(20),
    birthdate DATE NOT NULL,
    UNIQUE(user_id)
);

-- 2. Bảng Khóa học (Quản lý mã môn, tín chỉ...)
CREATE TABLE IF NOT EXISTS courses (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    course_code VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(150) NOT NULL,
    credits INT NOT NULL,
    course_type VARCHAR(20) NOT NULL
);

-- 3. Bảng Phân công giảng dạy (Liên kết Giảng viên - Khóa học)
CREATE TABLE IF NOT EXISTS course_assignments (
    course_id UUID NOT NULL REFERENCES courses(id) ON DELETE CASCADE,
    teacher_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role_type VARCHAR(50) NOT NULL,
    PRIMARY KEY (course_id, teacher_id)
);

-- 4. Bảng Sinh viên đăng ký học (Liên kết Sinh viên - Khóa học)
CREATE TABLE IF NOT EXISTS student_courses (
    student_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    course_id UUID NOT NULL REFERENCES courses(id) ON DELETE CASCADE,
    grade DECIMAL(4,2),
    status VARCHAR(20),
    PRIMARY KEY (student_id, course_id)
);