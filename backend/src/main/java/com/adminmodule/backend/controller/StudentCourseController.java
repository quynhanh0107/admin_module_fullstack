package com.adminmodule.backend.controller;

import com.adminmodule.backend.dto.GradeInputDTO;
import com.adminmodule.backend.dto.StudentEnrollmentDTO;
import com.adminmodule.backend.entity.StudentCourse;
import com.adminmodule.backend.entity.Course;
import com.adminmodule.backend.service.StudentCourseService;
import lombok.RequiredArgsConstructor;

import java.util.UUID;
import java.util.List;
import java.util.Map;

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
    @PreAuthorize("hasAuthority('REGISTER_DEGREE') or hasAuthority('CREATE_CLASS') or hasRole('STUDENT')")
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

    // lấy danh sách học sinh theo khóa
    @GetMapping("/course/{courseId}/students")
    @PreAuthorize("hasAuthority('INPUT_GRADES') or hasAuthority('VIEW_COURSE')")
    public ResponseEntity<?> getStudentsByCourse(@PathVariable UUID courseId) {
        try {
            List<StudentCourse> students = studentCourseService.getStudentsByCourseId(courseId);
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi lấy danh sách học sinh");
        }
    }

    // lấy danh sách khóa học mà 1 giáo viên đang dạy
    @GetMapping("/teacher/{username}/courses")
    @PreAuthorize("hasAuthority('INPUT_GRADES')")
    public ResponseEntity<?> getCoursesByTeacher(@PathVariable String username) {
        try {
            // Nhờ bạn viết hàm này trong Service: Tìm các Course dựa theo username của giáo viên
            List<Course> courses = studentCourseService.getCoursesByTeacherUsername(username);
            return ResponseEntity.ok(courses);
           // return ResponseEntity.ok().build(); // Tạm để trống chờ bạn code logic Service
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi lấy danh sách môn học");
        }
    }

    @PostMapping("/scores") 
    @PreAuthorize("hasAuthority('INPUT_GRADES')")
    public ResponseEntity<?> saveScores(@RequestBody Map<String, Object> payload) {
        try {
            // Tạm thời để log để check xem payload gửi lên đúng không
            System.out.println("Payload nhận được: " + payload);
            
            // Xử lý logic lưu điểm nhiều học sinh ở đây (cần viết thêm hàm trong Service)
            studentCourseService.saveBatchScores(payload);
            
            return ResponseEntity.ok(Map.of("message", "Lưu điểm thành công!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi lưu điểm: " + e.getMessage());
        }
    }

    // Lấy danh sách môn học và điểm của một học sinh
    @GetMapping("/student/{userId}/scores")
    @PreAuthorize("hasAuthority('VIEW_OWN_TRANSCRIPT') or hasRole('STUDENT')")
    public ResponseEntity<?> getStudentScores(@PathVariable UUID userId) {
        try {
            // Bạn cần viết thêm hàm này trong file StudentCourseService
            List<StudentCourse> myScores = studentCourseService.getScoresByStudentId(userId);
            
            return ResponseEntity.ok(myScores);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi tải bảng điểm: " + e.getMessage());
        }
    }

    // Lấy toàn bộ danh sách đăng ký và điểm (Dành cho Admin)
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('CREATE_CLASS')")
    public ResponseEntity<?> getAllEnrollments() {
        try {
            // Cần viết thêm hàm getAllEnrollments trong Service
            List<StudentCourse> allData = studentCourseService.getAllEnrollments();
            return ResponseEntity.ok(allData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi tải dữ liệu tổng quan: " + e.getMessage());
        }
    }
}