package com.adminmodule.backend.service;

import com.adminmodule.backend.entity.Role;
import com.adminmodule.backend.entity.Action;
import com.adminmodule.backend.entity.User;
import com.adminmodule.backend.repository.RoleRepository;
import com.adminmodule.backend.repository.ActionRepository;
import com.adminmodule.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.List;

@Service
@RequiredArgsConstructor

public class RoleService {
    private final RoleRepository roleRepository;
    private final ActionRepository actionRepository;
    private final UserRepository userRepository;

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

    @Transactional
    public void deleteRole(String roleName) {
        // check Quyền xem có tồn tại không
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò: " + roleName));

        // ngăn chặn việc xóa các Quyền mặc định của hệ thống
        if (roleName.equals("ADMIN") || roleName.equals("USER")) {
            throw new RuntimeException("Không được phép xóa các vai trò mặc định của hệ thống!");
        }
        
        //ngăn chăn xóa nếu đang có người dùng role đó
        List<User> usersWithRole = userRepository.findByRoles_Name(roleName);
        if (!usersWithRole.isEmpty()) {
            throw new RuntimeException("Không thể xóa! Đang có " + usersWithRole.size() + " người dùng sử dụng vai trò này.");
        }

        // nếu an toàn, tiến hành xóa
        roleRepository.delete(role);
    }

    // lấy danh sách tất cả roles
    public List<Role> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles;
    }

    // Cấp 1 Quyền ứng với 1 Vai trò (áp dụng N-N)
    public Role assignActionToRole(String roleName, List<String> actionCodes) {
        Role role = roleRepository.findByName(roleName)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy role: " + roleName));

        List<Action> actions = actionRepository.findByCodeIn(actionCodes);
        
        // xóa danh sách cũ để xử lý trường hợp admin bỏ tick checkbox
        // role đang đóng vai trò là object.
        // khi gọi object role ra thì nó sẽ đưa danh sách (Set) để thêm action vào danh sách đó 
        role.getActions().clear();

        // thêm toàn bộ danh sách quyền mới được tick
        role.getActions().addAll(actions);
        
        // lưu vào DB
        // thực hiện @ManyToMany và lưu thay đổi vào bảng trung gian role_actions
        return roleRepository.save(role);
    }


}

