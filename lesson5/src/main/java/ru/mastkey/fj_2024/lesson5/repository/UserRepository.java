package ru.mastkey.fj_2024.lesson5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mastkey.fj_2024.lesson5.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
}
