package com.adminmodule.backend.controller;

import com.adminmodule.backend.entity.User;
import com.adminmodule.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController

// mọi đường dẫn API đều phải bắt đầu bằng http://localhost:8080/api/users
@RequestMapping("/api/users")

@RequiredArgsConstructor

public class UserController {
    private final UserService userService;

    // API dky tai khoan dung POST
    @PostMapping("/register") // chuyên nhận POST và sẽ chạy nếu client gọi .../api/users/register 
    public ResponseEntity<User> registerUser(@RequestBody Map<String, String> payload) {
        // lấy username và password (được gộp vào trong map) từ dữ liệu client gửi
        String username = payload.get("username");
        String password = payload.get("password");

        // Gọi Service để tạo người dùng mới
        User newUser = userService.createUser(username, password);
        
        return ResponseEntity.ok(newUser);
    }

    // API lấy thông tin user (dùng GET)
    @GetMapping("/{username}") // chuyên để đọc (GET) dữ liệu; {...} truyền một biến vào (VD: .../api/users/admin hay .../api/users/student)
    public ResponseEntity<User> getMethodName(@PathVariable String username) {
        // gọi service để tìm username 
        User user = userService.getUserByUsername(username);
        
        // trả về kqua cho client
        return ResponseEntity.ok(user);
    }

    // Thêm API sau khi viết RoleController.java
    // API Cấp Vai trò cho user
    // URL: POST http://localhost:8080/api/users/assign-role
    @PostMapping("/assign-role")
    public ResponseEntity<User> assignRoleToUser(@RequestBody Map<String, String> payload) {
        String userName = payload.get("username");
        String roleName = payload.get("roleName");
        
        User updatedUser = userService.assignRoleToUser(userName, roleName);

        return ResponseEntity.ok(updatedUser);
    }
    
}
