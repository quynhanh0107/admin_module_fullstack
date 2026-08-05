package com.adminmodule.backend.repository;

import com.adminmodule.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    
    // Tương đương với SELECT * FROM users Where username = ?
    Optional<User> findByUsername(String username);

    List<User> findByRoles_Name(String roleName);

    // kiểm tra xe username đã tồn tại chưa (dùng khi đky)
    boolean existsByUsername(String name);
}
