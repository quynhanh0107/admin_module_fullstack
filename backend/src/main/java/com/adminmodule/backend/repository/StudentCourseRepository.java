package com.adminmodule.backend.repository;

import com.adminmodule.backend.entity.StudentCourse;
import com.adminmodule.backend.entity.StudentCourseID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentCourseRepository extends JpaRepository<StudentCourse, StudentCourseID> {
    // tìm điểm của 1 hsinh trong 1 lớp cụ thể
    Optional<StudentCourse> findByStudentIdAndCourseId(UUID studentId, UUID courseId);
    
    // tìm toàn bộ bảng điểm của 1 hsinh
    List<StudentCourse> findByStudentId(UUID studentId);

    List<StudentCourse> findByCourseId(UUID courseId);
}