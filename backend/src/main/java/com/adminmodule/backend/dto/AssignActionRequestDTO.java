package com.adminmodule.backend.dto;

import java.util.List;

import lombok.Data;

@Data
public class AssignActionRequestDTO {
    private String roleName;
    private List<String> actionCode;
}
