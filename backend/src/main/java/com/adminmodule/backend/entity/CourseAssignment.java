package com.adminmodule.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "course_assignments")
@Data
public class CourseAssignment {

    // Nhúng khóa chính tổ hợp vào
    @EmbeddedId
    private CourseAssignmentID id = new CourseAssignmentID();

    // Liên kết với bảng Khóa học
    @ManyToOne
    @MapsId("courseId") // Map ID này vào khóa chính ở trên
    @JoinColumn(name = "course_id")
    private Course course;

    // Liên kết với bảng Người dùng (Giảng viên)
    @ManyToOne
    @MapsId("teacherId")
    @JoinColumn(name = "teacher_id")
    private User teacher;

    @Column(name = "role_type", nullable = false, length = 50)
    private String roleType; // VD: RESPONSIBLE_TEACHER, ASSISTANT_TEACHER
}