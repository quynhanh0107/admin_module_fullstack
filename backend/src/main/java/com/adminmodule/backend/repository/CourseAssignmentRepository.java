package com.adminmodule.backend.repository;

import com.adminmodule.backend.entity.CourseAssignment;
import com.adminmodule.backend.entity.CourseAssignmentID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CourseAssignmentRepository extends JpaRepository<CourseAssignment, CourseAssignmentID> {
    List<CourseAssignment> findByCourseId(UUID courseId);
}
