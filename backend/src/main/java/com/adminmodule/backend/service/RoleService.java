package com.adminmodule.backend.service;

import com.adminmodule.backend.entity.Role;
import com.adminmodule.backend.entity.Action;
import com.adminmodule.backend.repository.RoleRepository;
import com.adminmodule.backend.repository.ActionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class RoleService {
    private final RoleRepository roleRepository;
    private final ActionRepository actionRepository;

    // tạo Role mới
    public Role createRole(String roleName) {
        // ktra xem role đã tồn tại chưa
        Optional<Role> existingRole = roleRepository.findByName(roleName);
        if (existingRole.isPresent()) {
            throw new RuntimeException("Vai trò này đã tồn tại trong hệ thống!");
        }

        Role newRole = new Role();
        newRole.setName(roleName);

        // khởi tạo 1 danh sách quyền rỗng để tránh NULL
        newRole.setActions(new HashSet<>());

        return roleRepository.save(newRole);
    }

    // Cấp 1 Quyền ứng với 1 Vai trò (áp dụng N-N)
    public Role assignActionToRole(String roleName, String actionCode) {
        Role role = roleRepository.findByName(roleName)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy role: " + roleName));

        Action action = actionRepository.findByCode(actionCode)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy action: " + actionCode));
        
        // thêm action vào danh sách của role
        // role đang đóng vai trò là object.
        // khi gọi object role ra thì nó sẽ đưa danh sách (Set) để thêm action vào danh sách đó 
        role.getActions().add(action);
        
        // lưu vào DB
        // thực hiện @ManyToMany và lưu thay đổi vào bảng trung gian role_actions
        return roleRepository.save(role);
    }
}

