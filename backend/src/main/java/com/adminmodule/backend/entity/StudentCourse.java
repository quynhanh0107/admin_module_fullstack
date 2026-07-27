package com.adminmodule.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "student_courses")
@Data
public class StudentCourse {

    @EmbeddedId
    private StudentCourseID id = new StudentCourseID();

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    private Course course;

    // Dùng BigDecimal cho điểm số để có độ chính xác cao (VD: 8.50)
    @Column(precision = 4, scale = 2)
    private BigDecimal grade;

    @Column(length = 20)
    private String status; // VD: PASSED, FAILED, ENROLLED
}