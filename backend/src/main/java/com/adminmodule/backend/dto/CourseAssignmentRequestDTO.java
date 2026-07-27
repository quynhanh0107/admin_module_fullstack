package com.adminmodule.backend.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class CourseAssignmentRequestDTO {
    private UUID courseId;
    private UUID teacherId;
    private String roleType; // VD: "RESPONSIBLE_TEACHER" hoặc "ASSISTANT_TEACHER"
}