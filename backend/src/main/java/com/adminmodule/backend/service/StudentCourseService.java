package com.adminmodule.backend.service;

import com.adminmodule.backend.dto.GradeInputDTO;
import com.adminmodule.backend.dto.StudentEnrollmentDTO;
import com.adminmodule.backend.entity.Course;
import com.adminmodule.backend.entity.CourseAssignment;
import com.adminmodule.backend.entity.StudentCourse;
import com.adminmodule.backend.entity.StudentCourseID;
import com.adminmodule.backend.entity.User;
import com.adminmodule.backend.repository.CourseAssignmentRepository;
import com.adminmodule.backend.repository.CourseRepository;
import com.adminmodule.backend.repository.StudentCourseRepository;
import com.adminmodule.backend.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.stream.Collectors;

import java.util.UUID;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentCourseService {

    private final StudentCourseRepository studentCourseRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CourseAssignmentRepository courseAssignmentRepository;

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

    public List<StudentCourse> getStudentsByCourseId(UUID courseId) {
        return studentCourseRepository.findByCourseId(courseId);
    }

    // courses không liên quan đến giáo viên (trong bảng dlieu)
    // thay vào đó, nó liên kết với nhau qua bảng trung gian course_assignments
    // đề Để lấy được danh sách khóa học của một giáo viên, ta đi đường vòng:
    //  Từ Username -> Tìm các bản ghi phân công trong course_assignments -> Trích xuất khóa học từ các bản ghi đó.
    public List<Course> getCoursesByTeacherUsername (String username) {
        // trả về danh sách các object CourseAssignment
        // mỗi đối tượng này chứa thông tin về giáo viên và khóa học tương ứng
        List<CourseAssignment> assignments = courseAssignmentRepository.findByTeacher_Username(username);
        
        // trích xuất từ danh sách trên một danh sách các khóa học do giáo viên đó đảm nhiệm
        //.stream(): chuyển danh sách assignments thành 1 luồn dữ liệu để xử lý từng phần tử một cách tuần tự
        //.map(): biến đổi dữ liệu
        //CourseAssignment::getCourse: method reference; tuowg đương với việc viết 1 vòng lặp và gọi assignment.getCourse() với mỗi phần tử
        // .collect():Sau khi đã trích xuất được toàn bộ các đối tượng Course, hàm collect sẽ gom tất cả chúng lại và đóng gói thành một danh sách mới
        return assignments.stream()
                .map(CourseAssignment::getCourse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void saveBatchScores(Map<String, Object> payload) {
        // lấy courseid từ payload
        String courseIdStr = (String) payload.get("courseId");
        UUID courseId = UUID.fromString(courseIdStr);

        // lấy mảng danh sách điểm
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> scores = (List<Map<String, Object>>) payload.get("scores");

        // list gom các bản ghi cần cập nhật
        List<StudentCourse> enrollmentsToUpdate = new ArrayList<>();

        // loop qua từng học sinh để cập nhật điểm
        for (Map<String, Object> scoreData: scores) {
            String studentIdStr = (String) scoreData.get("studentId");
            UUID studentId = UUID.fromString(studentIdStr);

            Object gradeObj = scoreData.get("grade");

            if (gradeObj == null || gradeObj.toString().trim().isEmpty()) {
                continue; 
            }

            BigDecimal grade = new BigDecimal(gradeObj.toString());

            // tạo khóa chính tổ hợp để tìm kiếm dưới DB
            StudentCourseID id = new StudentCourseID();
            id.setCourseId(courseId);
            id.setStudentId(studentId);

            // tìm bản ghi ghi danh hiện tại
            StudentCourse enrollment = studentCourseRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy dữ liệu ghi danh của sinh viên có ID: " + studentIdStr));

            // cập nhật điểm
            enrollment.setGrade(grade);

            // xét trạng thái Pass/Fail (>= 2.0 thì qua môn)
            if (grade.doubleValue() >= 2.0) {
                enrollment.setStatus("PASSED");
            } else {
                enrollment.setStatus("FAILED");
            }

            // đưa vào danh sách chờ lưu
            enrollmentsToUpdate.add(enrollment);
        }

        // lưu TOÀN BỘ danh sách xuống db CÙNG 1 LÚC
        studentCourseRepository.saveAll(enrollmentsToUpdate);   
        
    }

    /**
     * Lấy danh sách tất cả các môn học và điểm số của một học sinh
     */
    public List<StudentCourse> getScoresByStudentId(UUID studentId) {
        // Trả về danh sách lấy được từ Database
        // Nhớ đổi tên hàm findBy... cho khớp với cái bạn vừa khai báo ở Repository
        return studentCourseRepository.findByStudentId(studentId);
    }

    public List<StudentCourse> getAllEnrollments() {
        return studentCourseRepository.findAll();
    }
}
