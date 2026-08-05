package com.adminmodule.backend.dto;

import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class JwtDTO {
    private String token;
    private String username;
    private List<String> roles;
    private List<String> actions;
    private UUID id;
}
