package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByRefreshToken(String token);

    Optional<User> findUserById(String id);

    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);
}
