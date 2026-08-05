package com.adminmodule.backend.service;

import com.adminmodule.backend.dto.CourseRequestDTO;
import com.adminmodule.backend.entity.Course;
import com.adminmodule.backend.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {
    
    private final CourseRepository courseRepository;

    public Course createCourse(CourseRequestDTO request) {
        // ktra course code 1 môn đã tồn tại chưa
        if (courseRepository.existsByCourseCode(request.getCourseCode())) {
            throw new RuntimeException("Mã khóa học này đã tồn tại trong hệ thống!");
        }

        // chuyển từ DTO sang Entity
        Course newCourse = new Course();
        newCourse.setCourseCode(request.getCourseCode());
        newCourse.setName(request.getName());
        newCourse.setCredits(request.getCredits());
        newCourse.setCourseType(request.getCourseType());

        //lưu xuống db
        return courseRepository.save(newCourse);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Transactional
    public Course updateCourse(String id, CourseRequestDTO request) {
        // check xem khóa học có tồn tại không
        Course course = courseRepository.findByCourseCode(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khóa học với code: " + id));
        
        if (!id.equals(request.getCourseCode()) && courseRepository.existsByCourseCode(request.getCourseCode())) {
            throw new RuntimeException("Mã khóa học mới đã tồn tại trong hệ thống!");
        }
        // cập nhật dữ liệu
        course.setCourseCode(request.getCourseCode());
        course.setName(request.getName());
        course.setCredits(request.getCredits());
        course.setCourseType(request.getCourseType());
        
        // lưu
        return courseRepository.save(course);
    }

    @Transactional
    public void deleteCourse(String id) {
        Course course = courseRepository.findByCourseCode(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khóa học với ID: " + id));
        
        // có thể cập nhật lên thành Soft delete (set boolean values)
        courseRepository.delete(course);

        courseRepository.flush();
    }

    
}