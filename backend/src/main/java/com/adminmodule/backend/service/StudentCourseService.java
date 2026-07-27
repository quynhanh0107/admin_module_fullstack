package com.adminmodule.backend.service;

import com.adminmodule.backend.dto.GradeInputDTO;
import com.adminmodule.backend.dto.StudentEnrollmentDTO;
import com.adminmodule.backend.entity.Course;
import com.adminmodule.backend.entity.StudentCourse;
import com.adminmodule.backend.entity.StudentCourseID;
import com.adminmodule.backend.entity.User;
import com.adminmodule.backend.repository.CourseRepository;
import com.adminmodule.backend.repository.StudentCourseRepository;
import com.adminmodule.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentCourseService {

    private final StudentCourseRepository studentCourseRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public StudentCourse enrollStudent(StudentEnrollmentDTO request) {
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khóa học!"));
        
        User student = userRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên!"));

        StudentCourseID studentCourseId = new StudentCourseID();
        studentCourseId.setCourseId(course.getId());
        studentCourseId.setStudentId(student.getId());

        // ktra xem sv đã đăng ký môn này chưa
        if (studentCourseRepository.existsById(studentCourseId)) {
            throw new RuntimeException("Sinh viên này đã đăng ký khóa học này rồi!");
        }

        //chuyển từ dto sang entity
        StudentCourse enrollment = new StudentCourse();
        enrollment.setId(studentCourseId);
        enrollment.setCourse(course);
        enrollment.setStudent(student);
        enrollment.setStatus("ENROLLED"); // set status mặc định
        // grade hiện tại chưa set, để null cho đến lúc nhập điểm

        return studentCourseRepository.save(enrollment);
    }

    public StudentCourse inputGrade(GradeInputDTO request) {
        // khởi tạo Khóa chính tổ hợp để tìm kiếm
        StudentCourseID id = new StudentCourseID();
        id.setCourseId(request.getCourseId());
        id.setStudentId(request.getStudentId());

        // tìm bản ghi ghi danh dưới db
        StudentCourse enrollment = studentCourseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sinh viên này chưa đăng ký khóa học!"));

        // cập nhật điểm số
        enrollment.setGrade(request.getGrade());

        // xét pass/fail (>= 2.0 thì qua môn)
        if (request.getGrade() != null) {
            if (request.getGrade().doubleValue() >= 2.0) {
                enrollment.setStatus("PASSED");
            } else {
                enrollment.setStatus("FAILED");
            }
        }

        return studentCourseRepository.save(enrollment);
    }
}
