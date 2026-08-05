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
    @PreAuthorize("hasAuthority('CREATE_CLASS') or hasRole('ADMIN')")
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

    // api update khóa học
    // URL: PUT http://localhost:8080/api/courses/{id}
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('UPDATE_CLASS') or hasRole('ADMIN')") // Tùy chỉnh lại tên quyền cho khớp hệ thống của bạn
    public ResponseEntity<?> updateCourse(@PathVariable String id, @RequestBody CourseRequestDTO request) {
        try {
            Course updatedCourse = courseService.updateCourse(id, request);
            return ResponseEntity.ok(updatedCourse);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    //api xóa khóa học
    // URL: DELETE http://localhost:8080/api/courses/{id}
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_CLASS') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteCourse(@PathVariable String id) {
        try {
            courseService.deleteCourse(id);
            // Trả về một JSON format để Angular dễ parse
            return ResponseEntity.ok(java.util.Map.of("message", "Đã xóa khóa học thành công"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}