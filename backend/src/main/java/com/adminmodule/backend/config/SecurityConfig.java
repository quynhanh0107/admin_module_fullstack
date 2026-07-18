package com.adminmodule.backend.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity // chỉ cho phép tần soát từng quyền

public class SecurityConfig {

    private final JwtFilter jwtFilter;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
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

    @Bean 
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        //cấp quyền truy cập cho cổng Frontend dc phép gọi API
        configuration.setAllowedOrigins(List.of("http://localhost:4200", "http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        //cho phép gửi các Header chứa dữ liệu, Authorization để gửi JWT
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        
        // set áp dụng cho toàn bộ API (từ /api/... trở đi)
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }   
}
