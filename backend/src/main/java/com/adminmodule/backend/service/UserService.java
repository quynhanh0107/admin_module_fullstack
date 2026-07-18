package com.adminmodule.backend.service;

import com.adminmodule.backend.entity.User;
import com.adminmodule.backend.entity.Role;
import com.adminmodule.backend.entity.Action;
import com.adminmodule.backend.repository.RoleRepository;
import com.adminmodule.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor


public class UserService {
    // inject (nhúng) repo UserRepository và ActionRepository
    // dùng keyword final để báo hiệu cho @RequiredArgsConstructor là biến bắt buộc
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    // thêm dòng này sau khi thêm BCrypt tại SecurityConfig.java
    private final PasswordEncoder passwordEncoder;

    // Tìm kiếm người dùng theo tên đăng nhập
    public User getUserByUsername(String username) {
        // gọi repo đi tìm dữ liệu và nhận về hộp Optional 
        Optional<User> userOptional = userRepository.findByUsername(username);

        // Mở hộp: ktra nếu có User thì lấy ra, nếu hộp rỗng thì trả về lỗi
        return userOptional.orElseThrow(() -> 
            new RuntimeException("Tài khoản không tồn tại")
        );
    }

    // Tạo mới 1 người dùng (đăng ký)
    public User createUser(String username, String rawPassword) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại!");
        }

        // Tạo object mới để truyền dữ liệu vào
        User newUser = new User();
        newUser.setUsername(username);

        // thêm dòng này sau khi thêm BCrypt tại SecurityConfig.java
        String encodedPassword = passwordEncoder.encode(rawPassword);
        newUser.setPasswordHash(encodedPassword);

        newUser.setIsActive(true);
        newUser.setRoles(new HashSet<>());

        // truyền dữ liệu vào Repo để lưu xuống DB
        return userRepository.save(newUser);
    }

    // thêm hàm này sau khi viết RoleService.java
    // Cấp 1 vai trò cho Người dùng
    public User assignRoleToUser(String userName, String roleName) {
        User user = userRepository.findByUsername(userName)
            .orElseThrow(() -> new RuntimeException( "Không tìm thấy username: " + userName));
        
        Role role = roleRepository.findByName(roleName)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy role: " + roleName));

        
        user.getRoles().add(role);

        return userRepository.save(user);

    }

    //kiểm tra đăng nhập: kiểm tra xem người dùng có tồn tại và đối chiếu mật khẩu đăng nhập
    public String verifyLogin(String username, String rawPassword) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại!"));
        
        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new BadCredentialsException("Tên đăng nhập hoặc mật khẩu không chính xác!");
        }

        // nếu trùng khớp mật khẩu, trả về username để in kèm với Token
        return user.getUsername();
    }

    // Thu thập toàn bộ vai trò và quyền của người dùng
    @Transactional(readOnly = true)
    public List<GrantedAuthority> getUserAuthorities(String username) {
        // tìm người dùng khả dụng
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new BadCredentialsException("Tên đăng nhập hoặc mật khẩu không chính xác!"));
        
        Set<GrantedAuthority> authorities = new HashSet<>();

        // loop through từng role
        for (Role role: user.getRoles()) {
            // Spring Security quy ước: Tên Role có chữ ROLE_ đứng trước
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));

            // loop through từng action trong role đó
            for (Action action: role.getActions()) {
                authorities.add(new SimpleGrantedAuthority(action.getCode()));
            }
        }
        // trả về danh sách quyền đã tổng hợp
        return new ArrayList<>(authorities);
    }
}


