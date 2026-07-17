package com.adminmodule.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity // chỉ cho phép tần soát từng quyền

public class SecurityConfig {

    private final JwtFilter jwtFilter;

    // khai báo công cụ Password Hash Bcrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // tắt chế độ bảo vệ csfr (để kết nối frontend/postman)
            .csrf(csfr -> csfr.disable())
            .authorizeHttpRequests(auth -> auth
                // cho phép mở cổng nếu thực hiện đăng nhập/dky
                .requestMatchers("/api/auth/login", "/api/users/register").permitAll()
                //tất cả các request khác sẽ bị khóa
                .anyRequest().authenticated()
            )
            // chèn bộ lọc jwtFilter trước bộ lọc mặc định của Spring Security (chặn các request và tìm kiếm username/password để xử lý)
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();

    }
}
