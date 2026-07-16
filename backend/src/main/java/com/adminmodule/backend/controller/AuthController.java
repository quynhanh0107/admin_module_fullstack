package com.adminmodule.backend.controller;

import com.adminmodule.backend.service.UserService;
import com.adminmodule.backend.util.JWTUtil;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")

public class AuthController {
    private final UserService userService;
    private final JWTUtil jwtUtil;

    // api đăng nhập
    // URL: POST http://localhost:8080/api/auth/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String password = payload.get("password");

        // kiểm tra đăng nhập xem đúng tên đăng nhập/mật khẩu không
        String validUsername = userService.verifyLogin(username, password);

        //tạo vé Token dựa vào tên đăng nhập
        String token = jwtUtil.generateToken(validUsername);

        // trả về token đính kèm message và token cho client
        return ResponseEntity.ok(Map.of(
            "message", "Đăng nhập thành công",
            "token", token
        ));
    }
}
