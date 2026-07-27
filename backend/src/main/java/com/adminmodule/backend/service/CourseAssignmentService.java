package com.adminmodule.backend.service;

import com.adminmodule.backend.dto.CourseAssignmentRequestDTO;
import com.adminmodule.backend.entity.Course;
import com.adminmodule.backend.entity.CourseAssignment;
import com.adminmodule.backend.entity.CourseAssignmentID;
import com.adminmodule.backend.entity.User;
import com.adminmodule.backend.repository.CourseAssignmentRepository;
import com.adminmodule.backend.repository.CourseRepository;
import com.adminmodule.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseAssignmentService {

    private final CourseAssignmentRepository assignmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CourseAssignment assignTeacher(CourseAssignmentRequestDTO request) {
        // ktra xem Khóa học và Giảng viên có tồn tại 
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khóa học!"));
        
        User teacher = userRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giảng viên!"));

        // later: có thể thêm code kiểm tra xem user này có thực sự mang Role TEACHER hay không)

        // khởi tạo Khóa chính tổ hợp (Composite Key)
        CourseAssignmentID assignmentId = new CourseAssignmentID();
        assignmentId.setCourseId(course.getId());
        assignmentId.setTeacherId(teacher.getId());

        // chuyển dữ liệu thành Entity
        CourseAssignment assignment = new CourseAssignment();
        assignment.setId(assignmentId);
        assignment.setCourse(course);
        assignment.setTeacher(teacher);
        assignment.setRoleType(request.getRoleType());

        // 4. Lưu xuống Database
        return assignmentRepository.save(assignment);
    }
}