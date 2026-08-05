package com.adminmodule.backend.controller;

import com.adminmodule.backend.entity.Role;
import com.adminmodule.backend.service.RoleService;
//import com.adminmodule.backend.service.UserService;
import com.adminmodule.backend.dto.AssignActionRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roles")

public class RoleController {
    //private final UserService userService;
    private final RoleService roleService;

    // api lấy toàn bộ vai trò
    @GetMapping
    @PreAuthorize("hasAuthority('VIEW_ROLE') or hasRole('ADMIN')")
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = this.roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    // API tạo vai trò mới 
    // URL: POST http://localhost:8080/api/roles
    @PostMapping()
    @PreAuthorize("hasAuthority('CREATE_ROLE') or hasRole('ADMIN')")
    public ResponseEntity<Role> createRole(@RequestBody Map<String, String> payload) {
        String roleName = payload.get("roleName");

        Role newRole = roleService.createRole(roleName);

        return ResponseEntity.ok(newRole);
    }

    // API Cấp 1 danh sách các quyền (Actions) cho 1 vai trò
    // URL: POST http://localhost::8080/api/roles/assign-roles
    @PostMapping("/assign-action")
    @PreAuthorize("hasAuthority('ASSIGN_ACTION') or hasRole('ADMIN')")
    public ResponseEntity<Role> assignActionToRole (@RequestBody AssignActionRequestDTO payload) {
        List<String> actionCode = payload.getActionCode();
        String roleName = payload.getRoleName();

        Role updatedRole = roleService.assignActionToRole(roleName, actionCode);

        return ResponseEntity.ok(updatedRole);
    }

    // API Xóa hoàn toàn 1 vai trò khỏi hệ thống
    // URL: DELETE http://localhost:8080/api/roles/{roleName}
    @DeleteMapping("/{roleName}")
    @PreAuthorize("hasAuthority('DELETE_ROLE') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteRole(@PathVariable String roleName) {
        
        roleService.deleteRole(roleName);
        
        return ResponseEntity.ok(Map.of("message", "Đã xóa vai trò [" + roleName + "] thành công"));
    }
}

