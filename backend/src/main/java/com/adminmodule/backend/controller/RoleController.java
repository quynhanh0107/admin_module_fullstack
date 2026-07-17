package com.adminmodule.backend.controller;

import com.adminmodule.backend.entity.Role;
import com.adminmodule.backend.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roles")

public class RoleController {
    private final RoleService roleService;

    // API tạo vai trò mới 
    // URL: POST http://localhost:8080/api/roles
    @PostMapping()
    @PreAuthorize("hasAuthority('CREATE_ROLE') or hasRole('ADMIN')")
    public ResponseEntity<Role> createRole(@RequestBody Map<String, String> payload) {
        String roleName = payload.get("roleName");

        Role newRole = roleService.createRole(roleName);

        return ResponseEntity.ok(newRole);
    }

    // API Cấp 1 quyền cho 1 vai trò
    // URL: POST http://localhost::8080/api/roles/assign-roles
    @PostMapping("/assign-action")
    @PreAuthorize("hasAuthority('ASSIGN_ACTION') or hasRole('ADMIN')")
    public ResponseEntity<Role> assignActionToRole (@RequestBody Map<String, String> payload) {
        String actionCode = payload.get("actionCode");
        String roleName = payload.get("roleName");

        Role updatedRole = roleService.assignActionToRole(roleName, actionCode);

        return ResponseEntity.ok(updatedRole);
    }
    
}
