package com.adminmodule.backend.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class StudentEnrollmentDTO {
    private UUID courseId;
    private UUID studentId;
}