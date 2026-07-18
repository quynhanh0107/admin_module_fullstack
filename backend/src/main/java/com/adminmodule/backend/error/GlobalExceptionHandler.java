package com.adminmodule.backend.error;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // lỗi từ chối quyền 403
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handlerAccessDeniedException(AccessDeniedException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("status", HttpStatus.FORBIDDEN.value());
        error.put("error", "Forbidden");
        error.put("message", "Quyền truy cập bị từ chối!");

        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    // handle lỗi sai tên đăng nhập hoặc mật khẩu (401)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handlerBadCredentialsException(BadCredentialsException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("status", HttpStatus.UNAUTHORIZED.value());
        error.put("error", "Unauthorized");
        error.put("message", "Tên đăng nhập hoặc mật khẩu không chính xác!");
        
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    // handle các lỗi chung khác (code logic, lỗi db...)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handlerGlobalException(Exception ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.put("error", "Internal Server Error");
        error.put("message", "Hệ thống gặp sự cố: " + ex.getMessage());
        
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
