package com.adminmodule.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 50)
    private String name; 

    @ManyToMany
    @JoinTable(
        name="role_actions",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "action_id")
    )
    private Set<Action> actions; // dùng set thay vì list để đảm bảo Role ko bị trùng
}
