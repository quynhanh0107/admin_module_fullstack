package com.adminmodule.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestSecurityController {

    // 1. Bất kỳ ai có Token đăng nhập (đã qua được JwtFilter) đều gọi được
    @GetMapping("/user")
    public ResponseEntity<String> testUserAccess() {
        return ResponseEntity.ok("Thành công: Bạn đã đăng nhập hợp lệ!");
    }

    // 2. Chỉ người có quyền CREATE_CLASS mới gọi được
    @GetMapping("/admin-only")
    @PreAuthorize("hasAuthority('CREATE_CLASS')")
    public ResponseEntity<String> testAdminAccess() {
        return ResponseEntity.ok("Thành công: Bạn có quyền CREATE_CLASS của ADMIN!");
    }

    // 3. Chỉ người có quyền INPUT_GRADES mới gọi được
    @GetMapping("/teacher-only")
    @PreAuthorize("hasAuthority('INPUT_GRADES')")
    public ResponseEntity<String> testTeacherAccess() {
        return ResponseEntity.ok("Thành công: Bạn có quyền INPUT_GRADES của TEACHER!");
    }
}