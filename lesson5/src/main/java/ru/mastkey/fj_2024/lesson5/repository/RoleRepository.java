package ru.mastkey.fj_2024.lesson5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mastkey.fj_2024.lesson5.entity.Role;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(String name);
}
