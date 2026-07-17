package com.adminmodule.backend.controller;

import com.adminmodule.backend.entity.Action;
import com.adminmodule.backend.service.ActionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/actions")
@RequiredArgsConstructor

public class ActionController {
    private final ActionService actionService;

    // API: Tạo quyền dùng POST
    // URl: http://localhost:8080/api/actions
    @PostMapping
    // chỉ user có quyền CREATE_ACTION mới được tạo
    @PreAuthorize("hasAuthority('CREATE_ACTION')")
    public ResponseEntity<Action> createAction(@RequestBody Map<String, String> payload) {
        String actionCode = payload.get("code");
        String module = payload.get("module");

        Action newAction = actionService.createAction(actionCode, module);
        
        return ResponseEntity.ok(newAction);
    }

    @GetMapping
    // đảm bảo có quyền VIEW_ACTION hoặc vai trò admin mới được truy cập
    @PreAuthorize("hasAuthority('VIEW_ACTION') or hasRole('ADMIN')")
    public ResponseEntity<List<Action>> getAllActions() {
        List<Action> actions = actionService.getActions();

        return ResponseEntity.ok(actions);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_ACTION')")
    public ResponseEntity<?> deleteAction(@PathVariable Long id) {
        return ResponseEntity.ok("Xóa Action thành công!");
    }
    
}
