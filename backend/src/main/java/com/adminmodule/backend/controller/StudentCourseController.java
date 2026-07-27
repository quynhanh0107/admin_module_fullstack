package com.adminmodule.backend.controller;

import com.adminmodule.backend.dto.GradeInputDTO;
import com.adminmodule.backend.dto.StudentEnrollmentDTO;
import com.adminmodule.backend.entity.StudentCourse;
import com.adminmodule.backend.service.StudentCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class StudentCourseController {

    private final StudentCourseService studentCourseService;

    // chỉ ai có quyền REGISTER_DEGREE hoặc CREATE_CLASS (Admin) mới được gọi
    @PostMapping
    @PreAuthorize("hasAuthority('REGISTER_DEGREE') or hasAuthority('CREATE_CLASS')")
    public ResponseEntity<?> enrollStudent(@RequestBody StudentEnrollmentDTO request) {
        try {
            StudentCourse enrollment = studentCourseService.enrollStudent(request);
            return ResponseEntity.ok(enrollment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // chỉ ai có quyền INPUT_GRADES (Giáo viên) 
    @PutMapping("/grade")
    @PreAuthorize("hasAuthority('INPUT_GRADES')")
    public ResponseEntity<?> inputGrade(@RequestBody GradeInputDTO request) {
        try {
            StudentCourse enrollment = studentCourseService.inputGrade(request);
            return ResponseEntity.ok(enrollment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}