package com.adminmodule.backend.dto;

import lombok.Data;

@Data
public class CourseRequestDTO {
    private String courseCode;
    private String name;
    private Integer credits;
    private String courseType;
}
