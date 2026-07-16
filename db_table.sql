CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- AUTH--
CREATE TABLE users (
	id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	username VARCHAR(50) UNIQUE NOT NULL,
	password_hash VARCHAR(255) NOT NULL,
	is_active BOOLEAN DEFAULT TRUE,
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE roles (
	id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE actions (
	id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	code UNIQUE VARCHAR(100) NOT NULL,
	module VARCHAR(50) NOT NULL
);

CREATE TABLE user_roles (
	--khóa ngoại tham chiếu sang id của bảng users và roles
	user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
	role_id UUID NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
	-- khóa chính tổ hợp dùng 2 cột ghép lại 
	-- từng cột riêng có thể trùng nhưng tổ hợp ghép lại thì KHÔNG
	PRIMARY KEY (user_id, role_id)
); 

CREATE TABLE role_actions (
	--khóa ngoại tham chiếu sang id của bảng roles và actions
	role_id UUID NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
	action_id UUID NOT NULL REFERENCES actions(id) ON DELETE CASCADE,
	PRIMARY KEY (role_id, action_id)
); 

CREATE TABLE refresh_tokens (
	id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	--khóa ngoại tham chiếu sang id của bảng users
	user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
	token VARCHAR(255) UNIQUE NOT NULL,
	expires_at TIMESTAMP NOT NULL,
	is_revoked BOOLEAN DEFAULT FALSE
); 

CREATE TABLE audit_logs (
	id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	--khóa ngoại tham chiếu sang id của bảng users
	user_id UUID REFERENCES users(id) ON DELETE SET NULL,
	action_type VARCHAR(50) NOT NULL,
	entity_name VARCHAR(100) NOT NULL,
	ip_address VARCHAR(45) NOT NULL,
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
); 

-- Class Management --
CREATE TABLE user_profiles (
	id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
	full_name VARCHAR(100) NOT NULL,
	identity_number VARCHAR(20) UNIQUE,
	birthdate DATE NOT NULL 
);

CREATE TABLE courses (
	id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	course_code VARCHAR(20) UNIQUE NOT NULL,
	name VARCHAR(150) NOT NULL,
	credits INT NOT NULL,
	course_type VARCHAR(20) NOT NULL
);

CREATE TABLE student_courses (
	student_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
	course_id UUID NOT NULL REFERENCES courses(id) ON DELETE CASCADE,
	grade DECIMAL(4,2),
	status VARCHAR(20) DEFAULT 'in_progress',
	PRIMARY KEY(student_id, course_id)
);

CREATE TABLE course_assignments (
	teacher_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
	course_id UUID NOT NULL REFERENCES courses(id) ON DELETE CASCADE,
	role_type VARCHAR(50),
	PRIMARY KEY(teacher_id, course_id)
);
