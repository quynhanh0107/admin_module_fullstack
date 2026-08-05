package com.adminmodule.backend.controller;

import com.adminmodule.backend.service.UserService;
import com.adminmodule.backend.entity.User;
import com.adminmodule.backend.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import com.adminmodule.backend.util.JWTUtil;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;
import java.util.HashMap;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")

public class AuthController {
    @Autowired
    private RefreshTokenService refreshTokenService;

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

        User user = userService.getUserByUsername(validUsername);

        // lấy danh sách các quyền khi đăng nhập
        List<String> roles = userService.getUserAuthorities(validUsername).stream()
                                        .map(GrantedAuthority::getAuthority)
                                        .collect(Collectors.toList());
        
        //tạo Token dựa vào tên đăng nhập
        String token_ngan_han = jwtUtil.generateToken(validUsername, roles);

        // tạo refresh token lưu vào Redis
        String refreshToken = refreshTokenService.createRefreshToken(validUsername);

        // gom lại để gửi về cho frontend
        Map<String, Object> response = new HashMap<>();
        response.put("token_ngan_han", token_ngan_han);
        response.put("refreshToken", refreshToken);
        response.put("username", validUsername);
        response.put("actions", roles);
        response.put("userId", user.getId());

        // trả về token đính kèm message và token cho client
        return ResponseEntity.ok(response);
    }

    // API cấp lại token_ngan_han
    @PostMapping("/refresh")
    public ResponseEntity<Map<String,Object>> refresh(@RequestBody Map<String, String> request) {
        String requestRefreshToken = request.get("refreshToken");

        String username = refreshTokenService.getRefreshToken(requestRefreshToken);

        if(username == null) {
            throw new RuntimeException("Refresh Token không hợp lệ hoặc đã hết hạn. Vui lòng đăng nhập lại!");
        }
        
        // khi cấp lại token cũng đi kèm với quyền
        List<String> roles = userService.getUserAuthorities(username).stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // cấp lại token ngắn hạn mới
        String newAccessToken = jwtUtil.generateToken(username, roles);

        Map<String, Object> response = new HashMap<>();
        response.put("token_ngan_han", newAccessToken);

        return ResponseEntity.ok(response);
    }

    // api đăng xuất (xóa token)
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        if (refreshToken != null && !refreshToken.isEmpty()) {
            refreshTokenService.deleteRefreshToken(refreshToken);
        }

        Map<String, String>  response = new HashMap<>();
        response.put("message", "Đã đăng xuất thành công");

        return ResponseEntity.ok(response);
    }
}
