package com.adminmodule.backend.controller;

import com.adminmodule.backend.dto.CourseRequestDTO;
import com.adminmodule.backend.entity.Course;
import com.adminmodule.backend.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    // chỉ nhg tkhoan có quyền CREATE_CLASS mới đi qua được cửa này
    // ko sẽ trả về 403
    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_CLASS')")
    public ResponseEntity<?> createCourse(@RequestBody CourseRequestDTO request) {
        try {
            Course course = courseService.createCourse(request);
            return ResponseEntity.ok(course);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // api lấy danh sách khóa học
    @GetMapping
    @PreAuthorize("isAuthenticated()") // phải đnhap thì mới được xem
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }
}