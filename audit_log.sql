CREATE TABLE IF NOT EXISTS audit_logs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID, -- Có thể null trong trường hợp user chưa đăng nhập (VD: Cố tình hack)
    action_type VARCHAR(50) NOT NULL, -- VD: CREATE_USER, INPUT_GRADES
    entity_name VARCHAR(100) NOT NULL, -- VD: Bảng Users, Bảng Courses
    ip_address VARCHAR(45) NOT NULL, -- Lưu địa chỉ IP
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);