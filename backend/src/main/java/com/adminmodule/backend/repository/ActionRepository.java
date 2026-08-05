package com.adminmodule.backend.repository;

import com.adminmodule.backend.entity.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Repository
public interface ActionRepository extends JpaRepository<Action, UUID> {
    Optional<Action> findByCode(String code);
    List<Action> findByCodeIn(List<String> actionCodes);
}
