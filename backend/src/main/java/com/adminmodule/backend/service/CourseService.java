package com.adminmodule.backend.service;

import com.adminmodule.backend.dto.CourseRequestDTO;
import com.adminmodule.backend.entity.Course;
import com.adminmodule.backend.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}