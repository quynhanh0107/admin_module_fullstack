package com.adminmodule.backend.repository;

import com.adminmodule.backend.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {
    // Tìm kiếm khóa học theo Mã môn (phục vụ việc check trùng lặp khi tạo mới)
    Optional<Course> findByCourseCode(String courseCode);
    boolean existsByCourseCode(String courseCode);
}