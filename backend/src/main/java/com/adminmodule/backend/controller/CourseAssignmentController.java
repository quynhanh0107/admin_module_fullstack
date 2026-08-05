package com.adminmodule.backend.controller;

import com.adminmodule.backend.dto.CourseAssignmentRequestDTO;
import com.adminmodule.backend.entity.CourseAssignment;
import com.adminmodule.backend.service.CourseAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
public class CourseAssignmentController {

    private final CourseAssignmentService assignmentService;

    // cChỉ ai có quyền phân công giáo viên mới được gọi
    @PostMapping
    @PreAuthorize("hasAuthority('ASSIGN_TEACHERS') or hasRole('ADMIN')")
    public ResponseEntity<?> assignTeacherToCourse(@RequestBody CourseAssignmentRequestDTO request) {
        try {
            CourseAssignment assignment = assignmentService.assignTeacher(request);
            return ResponseEntity.ok(assignment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
