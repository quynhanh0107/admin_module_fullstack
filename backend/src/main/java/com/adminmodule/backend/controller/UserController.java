package com.adminmodule.backend.controller;

import com.adminmodule.backend.dto.UserResponseDTO;
import com.adminmodule.backend.entity.User;
import com.adminmodule.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;


@RestController

// mọi đường dẫn API đều phải bắt đầu bằng http://localhost:8080/api/users
@RequestMapping("/api/users")

@RequiredArgsConstructor

public class UserController {
    private final UserService userService;

    // API dky tai khoan dung POST
    @PostMapping("/register") // chuyên nhận POST và sẽ chạy nếu client gọi .../api/users/register 
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody Map<String, String> payload) {
        // lấy username và password (được gộp vào trong map) từ dữ liệu client gửi
        String username = payload.get("username");
        String password = payload.get("password");

        // Gọi Service để tạo người dùng mới
        User newUser = userService.createUser(username, password);
        
        // chỉ lưu đưa những thông tin chung, ko bao gồm mật khẩu
        UserResponseDTO dto = new UserResponseDTO(newUser.getId(), newUser.getUsername());

        return ResponseEntity.ok(dto);
    }

    // API lấy thông tin user (dùng GET)
    @GetMapping("/{username}") // chuyên để đọc (GET) dữ liệu; {...} truyền một biến vào (VD: .../api/users/admin hay .../api/users/student)
    @PreAuthorize("hasAuthority('VIEW_USER') or hasRole('ADMIN')")
    public ResponseEntity<User> getMethodName(@PathVariable String username) {
        // gọi service để tìm username 
        User user = userService.getUserByUsername(username);
        
        // trả về kqua cho client
        return ResponseEntity.ok(user);
    }

    //api lấy danh sách tất cả người dùng
    @GetMapping
    @PreAuthorize("hasAuthority('VIEW_USER') or hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    // Thêm API sau khi viết RoleController.java
    // API Cấp Vai trò cho user
    // URL: POST http://localhost:8080/api/users/assign-role
    @PostMapping("/assign-role")
    @PreAuthorize("hasAuthority('ASSIGN_ROLE') or hasRole('ADMIN')")
    public ResponseEntity<User> assignRoleToUser(@RequestBody Map<String, String> payload) {
        String userName = payload.get("username");
        String roleName = payload.get("roleName");
        
        User updatedUser = userService.assignRoleToUser(userName, roleName);

        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{username}/roles/{roleName}")
    @PreAuthorize("hasAuthority('ASSIGN_ROLE') or hasRole('ADMIN')") // Phân quyền (bạn có thể đổi 'ASSIGN_ROLE' thành quyền phù hợp của hệ thống)
    public ResponseEntity<?> revokeRole(@PathVariable String username, @PathVariable String roleName) { 
        
        // Gọi xuống tầng Service để xử lý logic
        userService.revokeRoleFromUser(username, roleName);
        
        // Nên trả về dạng JSON (Map) thay vì chuỗi String trần để Angular dễ đọc hơn
        return ResponseEntity.ok(Map.of("message", "Đã thu hồi quyền thành công"));
    }

    // Endpoint xóa người dùng
    @DeleteMapping("/{username}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        try {
            userService.deleteUserByUsername(username); 
            return ResponseEntity.ok(Map.of("message", "Xóa tài khoản thành công!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi xóa tài khoản: " + e.getMessage());
        }
    }

}
