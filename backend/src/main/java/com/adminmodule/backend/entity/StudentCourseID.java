package com.adminmodule.backend.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Data
public class StudentCourseID implements Serializable {
    private UUID studentId;
    private UUID courseId;
}