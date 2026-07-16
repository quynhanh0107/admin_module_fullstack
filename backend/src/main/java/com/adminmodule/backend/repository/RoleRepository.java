package com.adminmodule.backend.repository;

import com.adminmodule.backend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID>{
    Optional<Role> findByName(String name); // tìm role tên role
}
